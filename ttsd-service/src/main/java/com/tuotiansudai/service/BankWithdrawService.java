package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WeChatUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankWithdrawService {

    private final BankWrapperClient bankWrapperClient;

    private final BankWithdrawMapper bankWithdrawMapper;

    private final BankAccountMapper bankAccountMapper;

    private final WeChatUserMapper weChatUserMapper;

    @Autowired
    public BankWithdrawService(BankWithdrawMapper bankWithdrawMapper, BankAccountMapper bankAccountMapper, WeChatUserMapper weChatUserMapper) {
        this.bankWrapperClient = new BankWrapperClient();
        this.bankWithdrawMapper = bankWithdrawMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.weChatUserMapper = weChatUserMapper;
    }

    public BankAsyncMessage withdraw(Source source, String loginName, String mobile, long amount, long fee) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        BankWithdrawModel bankWithdrawModel = new BankWithdrawModel(loginName, amount, fee, source);
        bankWithdrawMapper.create(bankWithdrawModel);

        Optional<WeChatUserModel> optional = weChatUserMapper.findByLoginName(loginName).stream().filter(WeChatUserModel::isBound).findFirst();
        return bankWrapperClient.withdraw(bankWithdrawModel.getId(), source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), amount, fee, optional.map(WeChatUserModel::getOpenid).orElse(null));
    }

    public long sumSuccessWithdrawByLoginName(String loginName) {
        return bankWithdrawMapper.sumSuccessWithdrawByLoginName(loginName);
    }
}
