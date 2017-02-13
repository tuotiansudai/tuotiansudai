package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class CouponExpiredSmsNotifyMessageConsumer implements MessageConsumer {

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponSmsExpiredNotify;
    }

    @Override
    public void consume(String message) {
        SmsCouponNotifyDto smsCouponNotifyDto;
        try {
            smsCouponNotifyDto = JsonConverter.readValue(message, SmsCouponNotifyDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        smsWrapperClient.sendCouponExpiredNotify(smsCouponNotifyDto);
    }
}
