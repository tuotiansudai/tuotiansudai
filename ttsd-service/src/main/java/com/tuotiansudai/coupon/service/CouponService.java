package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.exception.CreateCouponException;

import java.util.Date;
import java.util.List;

public interface CouponService {

    void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    void assignNewbieCoupon(String loginName);

    void editCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    List<CouponDto> findCoupons(int index, int pageSize);

    int findCouponsCount();

    CouponModel findCouponById (long couponId);

    long findEstimatedCount(UserGroup userGroup);

    List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, int index, int pageSize);

    int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime);

    void deleteCoupon(String loginName, long couponId);

    List<CouponDto> findInterestCoupons(int index, int pageSize);

    int findInterestCouponsCount();

    long estimateCouponExpectedInterest(long loanId, long couponId, long amount);

    List<CouponDto> findRedEnvelopeCoupons(int index, int pageSize);

    int findRedEnvelopeCouponsCount();
}
