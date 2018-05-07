package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

@Component
public class BankAccountRegisteredMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredMessageConsumer.class);

    private List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobilePhone", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_CreateBankAccount;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] CertificationSuccess_CreateBankAccount message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                String loginName = map.get("loginName");
                if (bankAccountMapper.findByLoginName(loginName) != null){
                    logger.info("[MQ] receive message: {}, user:{} completed bank account ", this.queue(), loginName);
                    return;
                }
                bankAccountMapper.create(new BankAccountModel(loginName,
                        map.get("userName"),
                        map.get("accountNo"),
                        map.get("orderNo"),
                        map.get("orderDate")));
                userMapper.updateUserNameAndIdentityNumber(loginName, map.get("realName"), map.get("identityCode"));
            }else {
                logger.error("[MQ] message is invalid {}", message);
            }

        }catch (Exception e){
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
