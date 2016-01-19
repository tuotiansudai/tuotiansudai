package com.tuotiansudai.coupon.util;

import org.springframework.stereotype.Service;

@Service
public class NewRegisteredUserCollector implements UserCollector {

    @Override
    public long count(long couponId) {
        return 0;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return true;
    }
}
