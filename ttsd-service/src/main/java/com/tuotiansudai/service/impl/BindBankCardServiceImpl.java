package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
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

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        if(accountModel == null){
            BaseDto<PayFormDataDto> baseDto = new BaseDto();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage("您尚未进行实名认证");
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }

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
