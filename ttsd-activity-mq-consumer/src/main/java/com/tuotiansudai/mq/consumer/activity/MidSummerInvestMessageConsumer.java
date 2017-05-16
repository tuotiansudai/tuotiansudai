package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.MidSummerInvestMapper;
import com.tuotiansudai.activity.repository.model.MidSummerInvestModel;
import com.tuotiansudai.message.InvestSuccessMidSummerMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class MidSummerInvestMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MidSummerInvestMessageConsumer.class);

    private final MidSummerInvestMapper midSummerInvestMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.endTime}\")}")
    private Date activityEndTime;

    @Autowired
    public MidSummerInvestMessageConsumer(MidSummerInvestMapper midSummerInvestMapper) {
        this.midSummerInvestMapper = midSummerInvestMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_MidSummer;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }

        try {
            InvestSuccessMidSummerMessage investSuccessMidSummerMessage = JsonConverter.readValue(message, InvestSuccessMidSummerMessage.class);

            midSummerInvestMapper.create(new MidSummerInvestModel(investSuccessMidSummerMessage.getInvestId(),
                    investSuccessMidSummerMessage.getAmount(),
                    investSuccessMidSummerMessage.getTradingTime(),
                    investSuccessMidSummerMessage.getLoginName(),
                    investSuccessMidSummerMessage.getReferrerLoginName()));

        } catch (IOException e) {
            logger.error("[MQ] parse message failed: {}: {}.", this.queue(), message);
        }
    }
}
