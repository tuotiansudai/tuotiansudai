package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponExchangeMapper {

    void create(CouponExchangeModel couponExchangeModel);

    List<CouponExchangeModel> findByCouponId(long couponId);
}
