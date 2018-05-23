package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MiPushClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankWithdrawPushMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawPushMessageConsumer.class);

    private final MiPushClient miPushClient;

    @Autowired
    public BankWithdrawPushMessageConsumer(MiPushClient miPushClient) {
        this.miPushClient = miPushClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_PushMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Withdraw_PushMessage message is empty");
            return;
        }

        try {
            BankWithdrawMessage bankWithdrawMessage = new Gson().fromJson(message, BankWithdrawMessage.class);
            if (!bankWithdrawMessage.isStatus()) {
                return;
            }

            String title = MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(bankWithdrawMessage.getAmount()));

            PushMessage pushMessage = new PushMessage(Lists.newArrayList(bankWithdrawMessage.getLoginName()),
                    PushSource.ALL,
                    PushType.WITHDRAW_SUCCESS,
                    title,
                    AppUrl.MESSAGE_CENTER_LIST);
            miPushClient.sendPushMessage(pushMessage);

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
