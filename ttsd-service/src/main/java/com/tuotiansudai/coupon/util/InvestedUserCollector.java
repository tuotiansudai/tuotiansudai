package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.mapper.InvestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Override
    public long count(long couponId) {
        return investMapper.findInvestorCount();
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return investMapper.hasSuccessInvest(loginName);
    }
}
