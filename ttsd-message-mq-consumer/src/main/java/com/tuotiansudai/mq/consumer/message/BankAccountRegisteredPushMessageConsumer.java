package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

@Component
public class BankAccountRegisteredPushMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredPushMessageConsumer.class);

    private List<String> JSON_KEYS = Lists.newArrayList("mobilePhone", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_PushMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] CertificationSuccess_PushMessage message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());

            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                UserModel userModel = userMapper.findByMobile(map.get("mobilePhone"));
                pointTaskService.completeNewbieTask(PointTask.REGISTER, userModel.getLoginName());
            }else {
                logger.error("[MQ] message is invalid {}", message);
            }

        }catch (Exception e){
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
