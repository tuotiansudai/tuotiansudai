package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findCouponById(long id);

    void updateCoupon(CouponModel couponModel);

    List<CouponModel> findCoupons(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    int findCouponsCount();

}
