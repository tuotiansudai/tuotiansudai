package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankRechargeService {

    private static Logger logger = LoggerFactory.getLogger(BankRechargeService.class);

    private final BankRechargeMapper bankRechargeMapper;

    private final BankAccountMapper bankAccountMapper;

    private final MQWrapperClient mqWrapperClient;

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    public BankRechargeService(BankRechargeMapper bankRechargeMapper, BankAccountMapper bankAccountMapper, MQWrapperClient mqWrapperClient) {
        this.bankRechargeMapper = bankRechargeMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BankAsyncMessage recharge(Source source, String loginName, String mobile, long amount, String payType, String channel, Boolean isLoaner) {
        if (isLoaner == null){
            return new BankAsyncMessage(null, null, false, "充值失败");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, isLoaner ? Role.LOANER.name() : Role.INVESTOR.name());
        BankRechargeModel bankRechargeModel = new BankRechargeModel(loginName, amount, payType, source, channel);
        if (isLoaner){
            bankRechargeMapper.createLoaner(bankRechargeModel);
        }else {
            bankRechargeMapper.createInvestor(bankRechargeModel);
        }
        return bankWrapperClient.recharge(bankRechargeModel.getId(), source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), amount, payType);
    }

    @Transactional
    public void processRecharge(BankRechargeMessage bankRechargeMessage) {

        BankRechargeModel userRechargeModel = bankRechargeMapper.findById(bankRechargeMessage.getRechargeId());

        if (userRechargeModel.getStatus() != BankRechargeStatus.WAIT_PAY) {
            logger.error("userRechargeModel statue is not wait, rechargeId: {} ", bankRechargeMessage.getRechargeId());
            return;
        }
        userRechargeModel.setStatus(bankRechargeMessage.isStatus() ? BankRechargeStatus.SUCCESS : BankRechargeStatus.FAIL);
        userRechargeModel.setBankOrderNo(bankRechargeMessage.getBankOrderNo());
        userRechargeModel.setBankOrderDate(bankRechargeMessage.getBankOrderDate());
        bankRechargeMapper.update(userRechargeModel);


        if (bankRechargeMessage.isStatus()) {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                    Lists.newArrayList(new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                            bankRechargeMessage.getLoginName(),
                            bankRechargeMessage.getRechargeId(),
                            bankRechargeMessage.getBankOrderNo(),
                            bankRechargeMessage.getBankOrderDate(),
                            userRechargeModel.getAmount(),
                            UserBillBusinessType.RECHARGE_SUCCESS)));
        }
    }

    public long sumSuccessRechargeAmount(String loginName, boolean isLoaner) {
        return bankRechargeMapper.sumRechargeSuccessAmountByLoginNameAndRole(loginName, isLoaner ? Role.LOANER.name() : Role.INVESTOR.name());
    }

    public BankRechargeModel findRechargeById(long id) {
        return bankRechargeMapper.findById(id);
    }
}
