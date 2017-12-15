package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestCouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MobileAppInvestCouponServiceImpl implements MobileAppInvestCouponService {

    static Logger logger = Logger.getLogger(MobileAppInvestCouponServiceImpl.class);

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

    @Autowired
    private UserMapper userMapper;

    private Map<CouponType, Integer> dicRule = Maps.newHashMap(ImmutableMap.<CouponType, Integer>builder()
            .put(CouponType.RED_ENVELOPE, 100)
            .put(CouponType.BIRTHDAY_COUPON, 99)
            .put(CouponType.NEWBIE_COUPON, 98)
            .put(CouponType.INVEST_COUPON, 97)
            .put(CouponType.INTEREST_COUPON, 96)
            .build());

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Override
    public BaseResponseDto getInvestCoupons(InvestRequestDto dto) {
        String loanId = dto.getLoanId();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }

        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));

        if (loanModel == null) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }

        List<UserCouponModel> userCouponModels = new ArrayList<>();
        if (!loanDetailsMapper.getByLoanId(Long.parseLong(loanId)).getDisableCoupon()) {
            userCouponModels = userCouponMapper.findUserCouponWithCouponByLoginName(dto.getBaseParam().getUserId(), null);
        }

        List<UserCouponModel> unavailableCouponList = filterUnavailableLoanProductType(dto.getBaseParam().getUserId(), userCouponModels, loanModel.getProductType());

        userCouponModels.removeAll(unavailableCouponList);

        sortCoupons(userCouponModels, AmountConverter.convertStringToCent(dto.getInvestMoney()));

        userCouponModels.addAll(unavailableCouponList);

        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                boolean used = InvestStatus.SUCCESS == userCouponModel.getStatus()
                        || (userCouponModel.getUsedTime() != null && new DateTime(userCouponModel.getUsedTime()).plusSeconds(couponLockSeconds).isAfter(new DateTime()));
                boolean expired = !used && userCouponModel.getEndTime().before(new Date());
                return !used && !expired;

            }
        });
        Iterator<BaseCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, BaseCouponResponseDataDto>() {
            @Override
            public BaseCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                return new BaseCouponResponseDataDto(userCouponModel);
            }
        });

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(new UserCouponListResponseDataDto(Lists.newArrayList(items)));
        return responseDto;
    }

    //删除3元红包和项目类型不匹配的券和红包
    private List<UserCouponModel> filterUnavailableLoanProductType(String loginName, List<UserCouponModel> userCouponModels, ProductType loanProductType) {
        List<UserCouponModel> unavailableCoupons = Lists.newArrayList();
        UserModel userModel = userMapper.findByLoginName(loginName);

        for (UserCouponModel item : userCouponModels) {
            CouponModel couponModel = item.getCoupon();
            if (!couponModel.getProductTypes().contains(loanProductType) || CouponType.BIRTHDAY_COUPON.equals(couponModel.getCouponType()) && !UserBirthdayUtil.isBirthMonth(userModel.getIdentityNumber())) {
                unavailableCoupons.add(item);
            }
        }

        return unavailableCoupons;
    }

    private void sortCoupons(List<UserCouponModel> userCouponModels, final long amount) {
        Collections.sort(userCouponModels, new Comparator<UserCouponModel>() {
            @Override
            public int compare(UserCouponModel first, UserCouponModel second) {
                CouponModel firstCoupon = first.getCoupon();
                CouponModel secondCoupon = second.getCoupon();

                boolean firstCouponAvailable = isAvailableCoupon(firstCoupon, amount);
                boolean secondCouponAvailable = isAvailableCoupon(secondCoupon, amount);
                if (firstCouponAvailable && secondCouponAvailable) {
                    return Ints.compare(dicRule.get(secondCoupon.getCouponType()), dicRule.get(firstCoupon.getCouponType()));
                }
                if (firstCouponAvailable) {
                    return -1;
                }
                if (secondCouponAvailable) {
                    return 1;
                }
                return Ints.compare(dicRule.get(secondCoupon.getCouponType()), dicRule.get(firstCoupon.getCouponType()));
            }
        });
    }

    private boolean isAvailableCoupon(CouponModel couponModel, long amount) {
        return couponModel.getCouponType() == CouponType.INTEREST_COUPON && amount >= couponModel.getInvestLowerLimit()
                || couponModel.getCouponType() != CouponType.INTEREST_COUPON && amount >= couponModel.getInvestLowerLimit();
    }
}
