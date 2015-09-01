package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InsufficientBalanceException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class InvestServiceImpl implements InvestService {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestMapper mapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) {
        String loginName = LoginUserInfo.getLoginName();
        investDto.setLoginName(loginName);
        investDto.setInvestSource(InvestSource.WEB);
        return payWrapperClient.invest(investDto);
    }

    @Override
    public long getSuccessInvestedAmountByLoanId(long loanId) {
        return 0;
    }

    @Override
    public List<InvestModel> getByLoanId(long loanId, int rowLimit, int rowIndex) {
        return null;
    }
}
