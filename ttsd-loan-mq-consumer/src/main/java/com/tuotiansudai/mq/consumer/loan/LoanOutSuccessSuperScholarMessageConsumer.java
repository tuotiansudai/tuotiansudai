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

import java.io.IOException;

@Component
public class LoanOutSuccessSuperScholarMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessSuperScholarMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public static final String SUPER_SCHOLAR_SEND_CASH_LOAN = "SUPER_SCHOLAR_SEND_CASH_LOAN";

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_SuperScholarActivity;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_SuperScholarActivity receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_SuperScholarActivity receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_SuperScholarActivity loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_SuperScholarActivity json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }
//        redisWrapperClient.hset(SUPER_SCHOLAR_SEND_CASH_LOAN, String.valueOf(loanOutInfo.getLoanId()), DateTime.now().plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        redisWrapperClient.hset(SUPER_SCHOLAR_SEND_CASH_LOAN, String.valueOf(loanOutInfo.getLoanId()), DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }

}
