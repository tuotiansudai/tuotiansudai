package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class BankAccountRegisteredMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredMessageConsumer.class);

    private List<String> JSON_KEY = Lists.newArrayList("mobilePhone", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_CreateBankAccount;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] CertificationSuccess_CreateBankAccount message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            UserModel userModel = userMapper.findByMobile(map.get("mobilePhone"));
            if (bankAccountMapper.findByLoginName(userModel.getLoginName()) != null){
                logger.info("[MQ] receive message: {}, user:{} completed bank account ", this.queue(), userModel.getLoginName());
                return;
            }

            if (true) {
                bankAccountMapper.create(new BankAccountModel(userModel.getLoginName(),
                        map.get("userName"),
                        map.get("accountNo"),
                        map.get("orderNo"),
                        map.get("orderDate")));
                userMapper.updateUserNameAndIdentityNumber(userModel.getLoginName(), map.get("realName"), map.get("identityCode"));
            }

        }catch (Exception e){
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }

    }
}
