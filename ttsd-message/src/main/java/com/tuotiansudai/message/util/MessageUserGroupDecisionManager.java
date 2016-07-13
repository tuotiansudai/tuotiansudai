package com.tuotiansudai.message.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.service.impl.MessageServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageUserGroupDecisionManager {

    static Logger logger = Logger.getLogger(MessageUserGroupDecisionManager.class);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public boolean decide(final String loginName, final long messageId) {
        MessageModel messageModel = this.messageMapper.findById(messageId);
        List<MessageUserGroup> userGroups = messageModel.getUserGroups();

        return Iterators.any(userGroups.iterator(), new Predicate<MessageUserGroup>() {
            @Override
            public boolean apply(MessageUserGroup userGroup) {
                return MessageUserGroupDecisionManager.this.contains(loginName, userGroup, messageId);
            }
        });
    }

    @SuppressWarnings(value = "unchecked")
    private boolean contains(String loginName, MessageUserGroup messageUserGroup, long messageId) {
        switch (messageUserGroup) {
            case ALL_USER:
                return true;
            case IMPORT_USER:
                boolean contains = false;
                try {
                    List<String> loginNames = (List<String>) redisWrapperClient.hgetSeri(MessageServiceImpl.redisMessageReceivers, String.valueOf(messageId));
                    contains = loginNames.contains(loginName);
                } catch (Exception e) {
                    logger.error("[MessageUserGroupDecisionManager][NameList contains loginName]" + e.getMessage());
                }
                return contains;
        }
        return false;
    }
}
