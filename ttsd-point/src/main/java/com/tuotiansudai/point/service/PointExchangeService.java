package com.tuotiansudai.point.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;

import java.util.List;

public interface PointExchangeService {

    List<ExchangeCouponDto> findExchangeableCouponList();

    boolean exchangeCoupon(long couponId, String loginName, long exchangePoint);

    boolean exchangeableCoupon(long couponId, String loginName);

    List<ProductOrderViewDto> findProductOrderListByLoginName(String loginName, int index, int pageSize);

    long findProductOrderListByLoginNameCount(String loginName);

}
