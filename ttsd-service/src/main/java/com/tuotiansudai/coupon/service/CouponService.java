package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.exception.CreateCouponException;

import java.util.List;

public interface CouponService {

    void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    void assignNewbieCoupon(String loginName);

    void editCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    List<CouponDto> findCoupons(int index, int pageSize);

    int findCouponsCount();

    CouponModel findCouponById (long couponId);

    long findEstimatedCount(UserGroup userGroup);

    List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed);

    void deleteCoupon(String loginName, long couponId);
}
