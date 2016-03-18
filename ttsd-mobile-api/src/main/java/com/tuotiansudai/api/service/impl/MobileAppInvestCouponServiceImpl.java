package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppInvestCouponService;

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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MobileAppInvestCouponServiceImpl implements MobileAppInvestCouponService {
    static Logger log = Logger.getLogger(MobileAppInvestCouponServiceImpl.class);
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private CouponMapper couponMapper;
    private HashMap<CouponType, Integer> dicRule = Maps.newHashMap(ImmutableMap.<CouponType, Integer>builder().put(CouponType.RED_ENVELOPE, 100)
            .put(CouponType.BIRTHDAY_COUPON, 99)
            .put(CouponType.NEWBIE_COUPON, 98)
            .put(CouponType.INVEST_COUPON, 97)
            .put(CouponType.INTEREST_COUPON, 96).build());

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

    @Override
    public BaseResponseDto getInvestCoupons(InvestRequestDto dto) {
        String loanId = dto.getLoanId();
        String investMoney = dto.getInvestMoney();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        if(StringUtils.isEmpty(investMoney)){
            investMoney = "0.00";
        }


        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));

        if (loanModel == null) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        long investMoneyLong = AmountConverter.convertStringToCent(dto.getInvestMoney());


        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(dto.getBaseParam().getUserId(), null);

        userCouponModels = filterUserCouponModels(userCouponModels, loanModel.getProductType());

        sortCoupons(userCouponModels,investMoneyLong);


        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                boolean used = InvestStatus.SUCCESS == userCouponModel.getStatus();
                boolean expired = !used && userCouponModel.getEndTime().before(new Date());
                return !used && !expired;

            }
        });
        Iterator<UserCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, UserCouponResponseDataDto>() {
            @Override
            public UserCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                return new UserCouponResponseDataDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
            }
        });
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(new UserCouponListResponseDataDto(Lists.newArrayList(items)));
        return responseDto;
    }

    //删除3元红包和项目类型不匹配的券和红包
    private List<UserCouponModel> filterUserCouponModels(List<UserCouponModel> userCouponModels, ProductType loanProductType) {
        ArrayList<UserCouponModel> listDel = new ArrayList<>();

        for (int i = 0; i < userCouponModels.size(); i++) {
            UserCouponModel item = userCouponModels.get(i);
            CouponModel couponModel = couponMapper.findById(item.getCouponId());
            if(CouponType.BIRTHDAY_COUPON.equals(couponModel.getCouponType()) && !userBirthdayUtil.isBirthMonth(item.getLoginName())){
                listDel.add(item);
                continue;
            }
            boolean isSupportedLoanType = false;
            for (ProductType type : couponModel.getProductTypes()) {
                if (type.equals(loanProductType)) {
                    isSupportedLoanType = true;
                    break;
                }
            }

            if (!isSupportedLoanType) {
                listDel.add(item);
            }
        }

        userCouponModels.removeAll(listDel);

        return userCouponModels;
    }

    private void sortCoupons(List<UserCouponModel> userCouponModels,final long investMoney) {
        Collections.sort(userCouponModels, new Comparator<UserCouponModel>() {
            @Override
            public int compare(UserCouponModel first, UserCouponModel second) {
                CouponModel firstCoupon = couponMapper.findById(first.getCouponId());
                CouponModel secondCoupon = couponMapper.findById(second.getCouponId());

                if (isAvailableCoupon(firstCoupon,investMoney) && isAvailableCoupon(secondCoupon,investMoney)) {
                    return compareCouponType(firstCoupon.getCouponType(), secondCoupon.getCouponType());
                } else if (isAvailableCoupon(firstCoupon,investMoney) && !isAvailableCoupon(secondCoupon,investMoney)) {
                    return -1;
                } else if (!isAvailableCoupon(firstCoupon,investMoney) && isAvailableCoupon(secondCoupon,investMoney)) {
                    return 1;
                } else if (!isAvailableCoupon(firstCoupon,investMoney) && !isAvailableCoupon(secondCoupon,investMoney)) {
                    return compareCouponType(firstCoupon.getCouponType(), secondCoupon.getCouponType());
                }
                return 0;
            }
        });
    }

    private int compareCouponType(CouponType typeFirst, CouponType typeSecond) {
        try {
            if (dicRule.get(typeFirst) > dicRule.get(typeSecond)) {
                return -1;
            }
            if(dicRule.get(typeFirst) == dicRule.get(typeSecond)){
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(),e);
        }

        return 1;
    }

    private boolean isAvailableCoupon(CouponModel couponModel,long investMoney) {
        boolean result = false;
        if(couponModel.getCouponType().equals(CouponType.INTEREST_COUPON) && investMoney <= couponModel.getInvestUpperLimit() && investMoney >= couponModel.getInvestLowerLimit()){
            return true;
        }else if(!couponModel.getCouponType().equals(CouponType.INTEREST_COUPON) && investMoney >= couponModel.getInvestLowerLimit()){
            return true;
        }
        return result;
    }
}
