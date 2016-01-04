package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisteredNotInvestedUserCollector implements UserCollector {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public List<String> collect() {
        List<String> investorLoginNames = investMapper.findInvestorLoginNames();
        List<String> accountLoginNames = accountMapper.findLoginNames();
        accountLoginNames.removeAll(investorLoginNames);
        return accountLoginNames;
    }
}
