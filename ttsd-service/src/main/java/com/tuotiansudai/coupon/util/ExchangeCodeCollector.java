package com.tuotiansudai.coupon.util;


import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeCodeCollector implements UserCollector{
    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return true;
    }

}
