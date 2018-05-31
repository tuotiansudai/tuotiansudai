package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.BankRechargeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

@Component
public class BankRechargeMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(BankRechargeMessageConsumer.class);

    private List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobile", "rechargeId", "orderDate", "orderNo", "isSuccess");

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.Recharge_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Recharge_Success message is empty");
            return;
        }
        try {
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                long rechargeId = Long.parseLong(map.get("rechargeId"));
                BankRechargeModel userRechargeModel = bankRechargeMapper.findById(rechargeId);
                if (userRechargeModel == null) {
                    logger.error("[MQ] receive message : {}, userRechargeModel is null user:{}, rechargeId:{} ", this.queue(), map.get("loginName"), map.get("rechargeId"));
                    return;
                }

                if (userRechargeModel.getStatus() != BankRechargeStatus.WAIT_PAY) {
                    logger.error("[MQ] receive message : {}, userRechargeModel statue is not wait pay user:{}, rechargeId:{} ", this.queue(), map.get("loginName"), map.get("rechargeId"));
                    return;
                }

                boolean isSuccess = Boolean.valueOf(map.get("isSuccess"));
                userRechargeModel.setStatus(isSuccess ? BankRechargeStatus.SUCCESS : BankRechargeStatus.FAIL);
                userRechargeModel.setBankOrderNo(map.get("orderNo"));
                userRechargeModel.setBankOrderDate(map.get("orderDate"));
                bankRechargeMapper.update(userRechargeModel);

                if (isSuccess) {
                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                            new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                                    map.get("loginName"),
                                    rechargeId,
                                    userRechargeModel.getAmount(),
                                    UserBillBusinessType.RECHARGE_SUCCESS));
                }

            } else {
                logger.error("[MQ] message is invalid {}", message);
            }

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}

