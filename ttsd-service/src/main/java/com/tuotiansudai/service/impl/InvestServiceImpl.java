package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InterestInitiateType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.InterestCalculator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestServiceImpl implements InvestService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) {
        String loginName = LoginUserInfo.getLoginName();
        investDto.setLoginName(loginName);
        return payWrapperClient.invest(investDto);
    }

    @Override
    public long calculateExpectedInterest(long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        int repayTimes = loanModel.calculateLoanRepayTimes();
        LoanType loanType = loanModel.getType();

        int daysOfMonth = 30;
        int duration = loanModel.getPeriods();
        if (loanType.getLoanPeriodUnit() == LoanPeriodUnit.MONTH) {
            duration = repayTimes * daysOfMonth;
        }
        return InterestCalculator.calculateInterest(loanModel, amount * duration);
    }
}
