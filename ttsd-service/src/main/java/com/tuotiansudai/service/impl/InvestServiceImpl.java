package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
        return payWrapperClient.invest(investDto);
    }
}
