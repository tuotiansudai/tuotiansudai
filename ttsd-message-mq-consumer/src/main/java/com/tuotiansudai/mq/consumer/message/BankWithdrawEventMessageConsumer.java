package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class BankWithdrawEventMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawEventMessageConsumer.class);

    private final UserMessageMapper userMessageMapper;

    private final MessageMapper messageMapper;

    @Autowired
    public BankWithdrawEventMessageConsumer(UserMessageMapper userMessageMapper, MessageMapper messageMapper) {
        this.userMessageMapper = userMessageMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_EventMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Withdraw_EventMessage message is empty");
            return;
        }

        try {
            BankWithdrawMessage bankWithdrawMessage = new Gson().fromJson(message, BankWithdrawMessage.class);
            if (!bankWithdrawMessage.isStatus()) {
                return;
            }

            String title = MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(bankWithdrawMessage.getAmount()));
            String content = MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getContentTemplate(), AmountConverter.convertCentToString(bankWithdrawMessage.getAmount()));

            MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.WITHDRAW_SUCCESS);
            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), bankWithdrawMessage.getLoginName(), title, content, new Date());
            userMessageMapper.create(userMessageModel);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
