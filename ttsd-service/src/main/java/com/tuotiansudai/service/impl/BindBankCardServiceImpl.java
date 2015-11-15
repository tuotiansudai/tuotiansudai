package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BindBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;


    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        return payWrapperClient.bindBankCard(dto);
    }

    @Override
    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        return payWrapperClient.replaceBankCard(dto);
    }

    @Override
    public BankCardModel getPassedBankCard(String loginName) {
        return bankCardMapper.findPassedBankCardByLoginName(loginName);
    }
}
