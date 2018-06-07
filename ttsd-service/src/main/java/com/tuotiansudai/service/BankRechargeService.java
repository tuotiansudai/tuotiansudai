package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankRechargeService {

    private BankRechargeMapper bankRechargeMapper;

    private BankAccountMapper bankAccountMapper;

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    private BankRechargeService(BankRechargeMapper bankRechargeMapper, BankAccountMapper bankAccountMapper){
        this.bankRechargeMapper = bankRechargeMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    public BankAsyncMessage recharge(Source source, String loginName, String mobile, long amount, String payType, String channel){
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        BankRechargeModel bankRechargeModel = new BankRechargeModel(loginName, amount, payType, source, channel);
        bankRechargeMapper.create(bankRechargeModel);
        return bankWrapperClient.recharge(bankRechargeModel.getId(), source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), amount, payType);
    }

    public long sumSuccessRechargeAmount(String loginName){
        return bankRechargeMapper.sumRechargeSuccessAmountByLoginName(loginName);
    }


}
