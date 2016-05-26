package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisteredNotInvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<String> collect(long couponId) {
        List<String> investorLoginNames = investMapper.findInvestorLoginNames();
        List<String> accountLoginNames = accountMapper.findLoginNames();
        accountLoginNames.removeAll(investorLoginNames);
        return accountLoginNames;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) == 0;
    }
}
