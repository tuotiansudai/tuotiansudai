package com.tuotiansudai.api.service;


import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MobileAppUserMessageServiceTest extends ServiceTestBase {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional
    public void getUserMessages() {
        UserModel userModel = getFakeUser("test");
        userMapper.create(userModel);
        MessageModel messageModel = getFakeMessage(userModel.getLoginName());
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("email@tuotiansudai.com");
        fakeUser.setMobile("18900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

    private MessageModel getFakeMessage(String loginName) {
        MessageModel messageModel = new MessageModel();
        messageModel.setTitle("test");
        messageModel.setTemplate("测试模板猜猜猜");
        messageModel.setType(MessageType.MANUAL);
        List<MessageUserGroup> userGroups = new ArrayList<>();
        userGroups.add(MessageUserGroup.ALL_USER);
        messageModel.setUserGroups(userGroups);
        List<MessageChannel> channels = new ArrayList<>();
        channels.add(MessageChannel.APP);
        messageModel.setChannels(channels);
        messageModel.setStatus(MessageStatus.APPROVED);
        messageModel.setExpiredTime(new DateTime(new Date()).plusDays(1).toDate());
        messageModel.setCreatedBy(loginName);
        return messageModel;
    }

    private UserMessageModel fillUserMessage() {
        UserMessageModel messageModel = new UserMessageModel();
        return null;
    }
}
