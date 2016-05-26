package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.repository.mapper.InvestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class InvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Override
    public List<String> collect(long couponId) {
        List<String> successInvests = investMapper.findInvestorLoginNames();
        Set<String> investorLoginNames = Sets.newHashSet(successInvests);
        return Lists.newArrayList(investorLoginNames);
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0;
    }
}
