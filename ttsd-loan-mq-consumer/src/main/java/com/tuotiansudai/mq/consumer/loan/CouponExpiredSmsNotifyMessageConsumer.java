package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class CouponExpiredSmsNotifyMessageConsumer implements MessageConsumer {

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponSmsExpiredNotify;
    }

    private static Logger logger = Logger.getLogger(CouponExpiredSmsNotifyMessageConsumer.class);

    @Override
    public void consume(String message) {
        SmsCouponNotifyDto smsCouponNotifyDto;
        try {
            smsCouponNotifyDto = JsonConverter.readValue(message, SmsCouponNotifyDto.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[CouponExpiredSmsNotifyMessageConsumer][consume]sms dto parse failed. message:{0}", message), e);
            return;
        }
        try {
            smsWrapperClient.sendCouponExpiredNotify(smsCouponNotifyDto);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[CouponExpiredSmsNotifyMessageConsumer][consume]sms send failed. message:{0}", message), e);
        }
    }
}
