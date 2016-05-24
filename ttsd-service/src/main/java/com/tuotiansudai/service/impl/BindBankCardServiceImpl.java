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
import com.tuotiansudai.service.InvestRepayService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayService investRepayService;

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

    @Override
    public boolean isReplacing(String loginName) {
        return CollectionUtils.isNotEmpty(bankCardMapper.findApplyBankCardByLoginName(loginName));
    }

    @Override
    public boolean isManual(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel.getFreeze() > 0) {
            return true;
        }
        if (investRepayService.findSumRepayingCorpusByLoginName(loginName) > 0 || investRepayService.findSumRepayingInterestByLoginName(loginName) > 0) {
            return true;
        }
        Map<String, String> data = payWrapperClient.getUserStatus(loginName);
        if (Double.parseDouble(data.get("账户余额")) > 0) {
            return true;
        }
        return false;
    }
}
