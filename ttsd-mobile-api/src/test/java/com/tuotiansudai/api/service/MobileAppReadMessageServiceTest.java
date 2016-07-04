package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.service.v1_0.MobileAppReadMessageService;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppReadMessageServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppReadMessageService mobileAppReadMessageService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMessageMapper userMessageMapper;

    @Test
    public void shouldUpdateReadAndReadTimeByIdIsOk(){
        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER, MessageUserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(messageModel);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), creator.getLoginName(), messageModel.getTitle(), messageModel.getTemplate());
        userMessageModel.setRead(false);
        userMessageMapper.create(userMessageModel);
        mobileAppReadMessageService.updateReadMessage(String.valueOf(userMessageModel.getId()));
        assertThat(userMessageMapper.findById(userMessageModel.getId()).isRead(),is(true));
    }
}
