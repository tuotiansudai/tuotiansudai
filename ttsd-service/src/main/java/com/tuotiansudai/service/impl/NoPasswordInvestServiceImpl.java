package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.NoPasswordInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class NoPasswordInvestServiceImpl implements NoPasswordInvestService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    @Transactional
    public void enabledNoPasswordInvest(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
    }

    @Override
    @Transactional
    public void disabledNoPasswordInvest(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(false);
        accountMapper.update(accountModel);
    }

    @Override
    public BaseDto<PayFormDataDto> agreement(String loginName, AgreementDto agreementDto) {
        agreementDto.setLoginName(loginName);
        return payWrapperClient.agreement(agreementDto);
    }
}
