package com.tuotiansudai.coupon.util;

import java.util.List;

public interface UserCollector {

    List<String> collect(long couponId);

    boolean contains(long couponId, String loginName);
}
