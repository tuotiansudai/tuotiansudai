package com.tuotiansudai.mq.consumer.point;


import com.google.gson.Gson;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BankAccountRegisteredCompletePointTaskConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredCompletePointTaskConsumer.class);


    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_CompletePointTask;
    }

    @Override
    public void consume(String message) {

        try {

            Map map = new Gson().fromJson(message, Map.class);

        }catch (Exception e){

        }

    }
}
