package com.tuotiansudai.coupon.service;

public interface ExchangeCodeService {

    boolean generateExchangeCode(long couponId, int count);
}
