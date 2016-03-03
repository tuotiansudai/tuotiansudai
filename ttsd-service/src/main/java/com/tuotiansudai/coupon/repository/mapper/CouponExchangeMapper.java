package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponExchangeMapper {

    void create(CouponExchangeModel couponExchangeModel);

    void update(CouponExchangeModel couponExchangeModel);

    CouponExchangeModel findByCouponId(long couponId);

}
