package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.UserRechargeDto;
import com.tuotiansudai.fudian.dto.BankAsyncData;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserRechargeMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRechargeModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankRechargeService {

    private UserRechargeMapper userRechargeMapper;

    private BankAccountMapper bankAccountMapper;

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    private BankRechargeService(UserRechargeMapper userRechargeMapper, BankAccountMapper bankAccountMapper){
        this.userRechargeMapper = userRechargeMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    public BankAsyncData recharge(Source source, String loginName, String mobile, long amount, String payType){
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        UserRechargeModel userRechargeModel = new UserRechargeModel(loginName, amount, source, payType);
        userRechargeMapper.create(userRechargeModel);
        return bankWrapperClient.recharge(userRechargeModel.getId(), source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), amount, payType);
    }


}
