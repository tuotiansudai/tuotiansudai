package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class LoanOutSuccessInviteHelpMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessInviteHelpMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public static final String INVEST_HELP_WAIT_SEND_CASH = "INVEST_HELP_WAIT_SEND_CASH";

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_InviteHelpActivity;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_InviteHelpActivity receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_InviteHelpActivity receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_InviteHelpActivity loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_InviteHelpActivity json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }
//        redisWrapperClient.hset(INVEST_HELP_WAIT_SEND_CASH, String.valueOf(loanOutInfo.getLoanId()), DateTime.now().plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        redisWrapperClient.hset(INVEST_HELP_WAIT_SEND_CASH, String.valueOf(loanOutInfo.getLoanId()), DateTime.now().plusMinutes(30).toString("yyyy-MM-dd HH:mm:ss"));
    }

}
