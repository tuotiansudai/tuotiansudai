package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppUserMessageServiceTest extends ServiceTestBase {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private MessageMapper messageMapper;

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
        baseParam.setUserId("testFakeUser");
        messagesRequestDto.setBaseParam(baseParam);
        messagesRequestDto.setIndex(1);
        messagesRequestDto.setPageSize(10);
        BaseResponseDto<UserMessageResponseDataDto> baseResponseDto = mobileAppUserMessageService.getUserMessages(messagesRequestDto);

        assertThat("0000", is(baseResponseDto.getCode()));
    }

    @Test
    public void shouldUnreadMessageCount() {
        UserModel userModel = getFakeUser("testFakeUser");
        userMapper.create(userModel);
        MessageModel messageModel = getFakeMessage(userModel.getLoginName());
        messageMapper.create(messageModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testFakeUser");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto<MobileAppUnreadMessageCount> messageCountBaseResponseDto = mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);

        assertThat("0000", is(messageCountBaseResponseDto.getCode()));
        String unreadMessageKey = MessageFormat.format(UNREAD_MESSAGE_COUNT_ID_KEY, userModel.getLoginName());
        redisWrapperClient.del(unreadMessageKey);
    }


    private MessageModel getFakeMessage(String loginName) {
        MessageModel messageModel = new MessageModel();
        messageModel.setTitle("test");
        messageModel.setTemplate("测试模板猜猜猜");
        messageModel.setType(MessageType.MANUAL);
        messageModel.setUserGroup(MessageUserGroup.ALL_USER);
        messageModel.setChannels(Lists.newArrayList(MessageChannel.APP_MESSAGE));
        messageModel.setStatus(MessageStatus.APPROVED);
        messageModel.setValidStartTime(DateTime.parse("0001-01-01").toDate());
        messageModel.setValidEndTime(new DateTime(new Date()).plusDays(1).toDate());
        messageModel.setCreatedBy(loginName);
        messageModel.setCreatedTime(new Date());
        messageModel.setUpdatedBy(loginName);
        messageModel.setUpdatedTime(new Date());
        return messageModel;
    }

    @Test
    public void shouldUpdateReadAndReadTimeByIdIsOk() {
        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel messageModel = new MessageModel("title",
                "template",
                "messageText",
                MessageUserGroup.ALL_USER,
                MessageCategory.ACTIVITY,
                Lists.newArrayList(MessageChannel.WEBSITE),
                null,
                null,
                null,
                creator.getLoginName(),
                DateTime.parse("0001-01-01").toDate(),
                DateTime.parse("9999-12-31").toDate());
        messageModel.setActivatedTime(new Date());
        messageMapper.create(messageModel);


        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), creator.getLoginName(), messageModel.getTitle(), messageModel.getTemplate(), messageModel.getActivatedTime());
        userMessageModel.setRead(false);
        userMessageMapper.create(userMessageModel);
        mobileAppUserMessageService.updateReadMessage(String.valueOf(userMessageModel.getId()));
        assertThat(userMessageMapper.findById(userMessageModel.getId()).isRead(), is(true));
    }

}
