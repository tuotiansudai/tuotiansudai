package com.tuotiansudai.coupon.util;

public interface UserCollector {

    long count(long couponId);

    boolean contains(long couponId, String loginName);
}
