package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;


    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        String loginName = LoginUserInfo.getLoginName();
        dto.setLoginName(loginName);
        return payWrapperClient.bindBankCard(dto);
    }

    @Override
    public String getUserName() {
        String loginName = LoginUserInfo.getLoginName();

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel.getUserName();
    }

    @Override
    public BankCardModel getPassedBankCard() {
        String loginName = LoginUserInfo.getLoginName();

        return bankCardMapper.findByLoginName(loginName);
    }
}
