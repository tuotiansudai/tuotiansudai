package com.tuotiansudai.mq.consumer.point;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class BankAccountRegisteredCompletePointTaskConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredCompletePointTaskConsumer.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public PointTaskService pointTaskService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_CompletePointTask;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] CertificationSuccess_CompletePointTask message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            UserModel userModel = userMapper.findByMobile(map.get("mobilePhone"));
            pointTaskService.completeNewbieTask(PointTask.REGISTER, userModel.getLoginName());

        }catch (Exception e){
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }
}
