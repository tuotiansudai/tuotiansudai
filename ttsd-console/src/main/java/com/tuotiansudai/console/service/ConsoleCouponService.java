package com.tuotiansudai.console.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.exception.CreateCouponException;

import java.util.Date;
import java.util.List;

public interface ConsoleCouponService {
    ExchangeCouponDto createCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    void editCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime);

    boolean deleteCoupon(String loginName, long couponId);

    CouponModel findCouponById(long couponId);

    List<CouponDto> findBirthdayCoupons(int index, int pageSize);

    int findBirthdayCouponsCount();

    List<CouponDto> findRedEnvelopeCoupons(int index, int pageSize);

    int findRedEnvelopeCouponsCount();

    List<CouponDto> findInterestCoupons(int index, int pageSize);

    int findInterestCouponsCount();

    List<CouponDto> findNewbieAndInvestCoupons(int index, int pageSize);

    int findNewbieAndInvestCouponsCount();

    long findEstimatedCount(UserGroup userGroup);


    List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, int index, int pageSize);

}
