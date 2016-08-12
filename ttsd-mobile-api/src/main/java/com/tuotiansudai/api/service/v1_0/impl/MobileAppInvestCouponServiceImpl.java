package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestCouponService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
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
    private CouponMapper couponMapper;

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

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

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(dto.getBaseParam().getUserId(), null);

        List<UserCouponModel> unavailableCouponList = filterUnavailableLoanProductType(userCouponModels, loanModel.getProductType());

        userCouponModels.removeAll(unavailableCouponList);

        sortCoupons(userCouponModels, AmountConverter.convertStringToCent(dto.getInvestMoney()));

        userCouponModels.addAll(unavailableCouponList);

        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                boolean used = InvestStatus.SUCCESS == userCouponModel.getStatus()
                        || (userCouponModel.getUsedTime() != null && new DateTime(userCouponModel.getUsedTime()).plusSeconds(couponLockSeconds).isBefore(new DateTime()));
                boolean expired = !used && userCouponModel.getEndTime().before(new Date());
                return !used && !expired;

            }
        });
        Iterator<BaseCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, BaseCouponResponseDataDto>() {
            @Override
            public BaseCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                return new BaseCouponResponseDataDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
            }
        });

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(new UserCouponListResponseDataDto(Lists.newArrayList(items)));
        return responseDto;
    }

    //删除3元红包和项目类型不匹配的券和红包
    private List<UserCouponModel> filterUnavailableLoanProductType(List<UserCouponModel> userCouponModels, ProductType loanProductType) {
        List<UserCouponModel> unavailableCoupons = Lists.newArrayList();

        for (UserCouponModel item : userCouponModels) {
            CouponModel couponModel = couponMapper.findById(item.getCouponId());
            if (!couponModel.getProductTypes().contains(loanProductType) || CouponType.BIRTHDAY_COUPON.equals(couponModel.getCouponType()) && !userBirthdayUtil.isBirthMonth(item.getLoginName())) {
                unavailableCoupons.add(item);
            }
        }

        return unavailableCoupons;
    }

    private void sortCoupons(List<UserCouponModel> userCouponModels, final long amount) {
        Collections.sort(userCouponModels, new Comparator<UserCouponModel>() {
            @Override
            public int compare(UserCouponModel first, UserCouponModel second) {
                CouponModel firstCoupon = couponMapper.findById(first.getCouponId());
                CouponModel secondCoupon = couponMapper.findById(second.getCouponId());

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
