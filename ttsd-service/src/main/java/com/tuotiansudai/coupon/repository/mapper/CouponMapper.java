package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findCouponById(long id);

}
