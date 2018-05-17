package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.client.MiPushClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

@Component
public class BankRegisteredPushMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankRegisteredPushMessageConsumer.class);

    private List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobile", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    private final MiPushClient miPushClient;

    @Autowired
    public BankRegisteredPushMessageConsumer(MiPushClient miPushClient) {
        this.miPushClient = miPushClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.RegisterBankAccount_PushMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] RegisterBankAccount_PushMessage message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                PushMessage pushMessage = new PushMessage(Lists.newArrayList(map.get("loginName")),
                        PushSource.ALL,
                        PushType.REGISTER_ACCOUNT_SUCCESS,
                        MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                        AppUrl.MESSAGE_CENTER_LIST);
                miPushClient.sendPushMessage(pushMessage);
            }else {
                logger.error("[MQ] message is invalid {}", message);
            }

        }catch (Exception e){
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
