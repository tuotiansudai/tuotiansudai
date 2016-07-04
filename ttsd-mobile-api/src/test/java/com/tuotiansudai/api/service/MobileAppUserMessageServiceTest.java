package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MobileAppUserMessageServiceTest extends ServiceTestBase {


    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisWrapperClient redisClient;

    public static final String UNREAD_MESSAGE_COUNT_ID_KEY = "app:unread:message:count:ids:{0}";

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Test
    public void shouldUserMessages() {
        UserModel userModel = getFakeUser("testFakeUser");
        userMapper.create(userModel);
        MessageModel messageModel = getFakeMessage(userModel.getLoginName());
        messageMapper.create(messageModel);
        UserMessagesRequestDto messagesRequestDto = new UserMessagesRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        messagesRequestDto.setBaseParam(baseParam);
        messagesRequestDto.setIndex(1);
        messagesRequestDto.setPageSize(10);
        BaseResponseDto<UserMessageResponseDataDto> baseResponseDto = mobileAppUserMessageService.getUserMessages(messagesRequestDto);

        assertThat("0000", is(baseResponseDto.getCode()));
        assertThat(1L, is(baseResponseDto.getData().getTotalCount()));
        assertThat(1, is(baseResponseDto.getData().getMessages().size()));
    }

    @Test
    public void shouldUnreadMessageCount() {
        UserModel userModel = getFakeUser("testFakeUser");
        userMapper.create(userModel);
        MessageModel messageModel = getFakeMessage(userModel.getLoginName());
        messageMapper.create(messageModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto<MobileAppUnreadMessageCount> messageCountBaseResponseDto = mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);

        assertThat("0000", is(messageCountBaseResponseDto.getCode()));
        assertTrue(messageCountBaseResponseDto.getData().isNewMessage());
        assertThat(messageCountBaseResponseDto.getData().getUnreadMessageCount(), is(1l));
        String unreadMessageKey = MessageFormat.format(UNREAD_MESSAGE_COUNT_ID_KEY, userModel.getLoginName());
        redisClient.del(unreadMessageKey);
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
        messageModel.setCreatedTime(new Date());
        messageModel.setUpdatedBy(loginName);
        messageModel.setUpdatedTime(new Date());
        return messageModel;
    }

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
        mobileAppUserMessageService.updateReadMessage(String.valueOf(userMessageModel.getId()));
        assertThat(userMessageMapper.findById(userMessageModel.getId()).isRead(),is(true));
    }

}
