package com.tuotiansudai.coupon.service;

public interface ExchangeCodeService {

    boolean generateExchangeCode(long couponId, int count);

    String toBase31Prefix(long couponId);

    long getValueBase31(String prefix);
}
