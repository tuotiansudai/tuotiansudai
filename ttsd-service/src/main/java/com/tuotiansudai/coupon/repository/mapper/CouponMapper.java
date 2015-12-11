package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findCouponById(long id);

    CouponModel findCouponByName(String name);


    void updateCoupon(CouponModel couponModel);

    List<CouponModel> findValidCoupon();

    CouponModel lockByCoupon(@Param(value = "id") long id);

}
