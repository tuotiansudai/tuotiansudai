package com.tuotiansudai.mq.consumer.sms;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.sms.client.JianZhouSmsClient;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsNotifyMessageConsumer implements MessageConsumer{
    private static Logger logger = LoggerFactory.getLogger(SmsNotifyMessageConsumer.class);

    @Autowired
    private JianZhouSmsClient jianZhouSmsClient = JianZhouSmsClient.getClient();

    @Override
    public MessageQueue queue() {
        return MessageQueue.SmsNotify;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)){
            logger.error("[MQ] parse message failed: {}: '{}'.", this.queue(), message);
        }

        try {
            SmsNotifyDto smsNotifyDto = JsonConverter.readValue(message, SmsNotifyDto.class);
            jianZhouSmsClient.sendSms(smsNotifyDto.getMobiles(), smsNotifyDto.getJianZhouSmsTemplate(), smsNotifyDto.isVoice(), smsNotifyDto.getParams(), smsNotifyDto.getRequestIP());
        }catch (Exception e){
            logger.error("[MQ] 程序内部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }

    }
}
