package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.request.UmpRechargeRequestDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.UmpAmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmpRechargeService {

    private static Logger logger = LoggerFactory.getLogger(UmpRechargeService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final RechargeMapper rechargeMapper;

    private final AccountMapper accountMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public UmpRechargeService(RechargeMapper rechargeMapper, AccountMapper accountMapper, MQWrapperClient mqWrapperClient) {
        this.rechargeMapper = rechargeMapper;
        this.accountMapper = accountMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public UmpAsyncMessage recharge(UmpRechargeRequestDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        RechargeModel rechargeModel = new RechargeModel(dto);
        rechargeModel.setId(IdGenerator.generate());
        rechargeMapper.create(rechargeModel);
        return bankWrapperClient.umpRecharge(dto.getLoginName(), accountModel.getPayUserId(), rechargeModel.getId(), rechargeModel.getAmount(), rechargeModel.isFastPay(), rechargeModel.getBankCode());
    }

    public void processRecharge(UmpRechargeMessage umpRechargeMessage){
        RechargeModel model = rechargeMapper.findById(umpRechargeMessage.getRechargeId());
        if (model == null || model.getStatus() != BankRechargeStatus.WAIT_PAY) {
            logger.error("UmpRechargeModel not exist or status is not wait, rechargeId: {} ", umpRechargeMessage.getRechargeId());
            return;
        }
        rechargeMapper.updateStatus(umpRechargeMessage.getRechargeId(), umpRechargeMessage.isStatus() ? BankRechargeStatus.SUCCESS : BankRechargeStatus.FAIL);
        if (umpRechargeMessage.isStatus()){
            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer,
                    new UmpAmountTransferMessage(
                            UmpTransferType.TRANSFER_IN_BALANCE,
                            umpRechargeMessage.getLoginName(),
                            model.getId(),
                            umpRechargeMessage.getAmount(),
                            UserBillBusinessType.RECHARGE_SUCCESS,
                            null,
                            null));
        }
    }
}
