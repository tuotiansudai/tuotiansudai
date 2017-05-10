package com.tuotiansudai.message.util;

import com.google.common.base.Strings;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageUserGroupDecisionManager {

    private static Logger logger = Logger.getLogger(MessageUserGroupDecisionManager.class);

    private final static String MESSAGE_IMPORT_USER_KEY = "message:manual-message:receivers";

    private final MessageMapper messageMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    public MessageUserGroupDecisionManager(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public boolean decide(final String loginName, String mobile, final long messageId) {
        MessageModel messageModel = this.messageMapper.findActiveById(messageId);
        MessageUserGroup userGroup = messageModel.getUserGroup();

        return this.contains(loginName, mobile, userGroup, messageId);
    }

    @SuppressWarnings(value = "unchecked")
    private boolean contains(String loginName, String mobile, MessageUserGroup messageUserGroup, long messageId) {
        if (Strings.isNullOrEmpty(loginName)) {
            return false;
        }
        switch (messageUserGroup) {
            case ALL_USER:
                return true;
            case IMPORT_USER:
                try {
                    List<String> loginNameOrMobiles = (List<String>) redisWrapperClient.hgetSeri(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageId));
                    return CollectionUtils.isNotEmpty(loginNameOrMobiles) && (loginNameOrMobiles.contains(loginName) || loginNameOrMobiles.contains(mobile));
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
        }
        return false;
    }
}
