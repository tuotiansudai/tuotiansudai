package com.tuotiansudai.mq.consumer.sms;

import com.google.common.base.Strings;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageConsumer implements MessageConsumer{
    private static Logger logger = LoggerFactory.getLogger(SmsMessageConsumer.class);
//
//    @Autowired
//    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserSms;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)){
            logger.error("[MQ] parse message failed: {}: '{}'.", this.queue(), message);
        }

        try {
//            SmsDto smsDto = JsonConverter.readValue(message, SmsDto.class);
//            smsWrapperClient.sendSms(smsDto);

        }catch (Exception e){
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }

    }
}
