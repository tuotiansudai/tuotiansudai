package com.tuotiansudai.coupon.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class InvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Override
    public List<String> collect() {
        List<String> successInvests = investMapper.findInvestorLoginNames();
        Set<String> investorLoginNames = Sets.newHashSet(successInvests);
        return Lists.newArrayList(investorLoginNames);
    }
}
