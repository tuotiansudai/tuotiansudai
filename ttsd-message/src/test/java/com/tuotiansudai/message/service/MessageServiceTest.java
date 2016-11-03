package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MessageServiceTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageService messageService;

    @Test
    public void shouldFindMessageList() throws Exception {

        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel manualMessageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.APPROVED, new Date(), creator.getLoginName());

        messageMapper.create(manualMessageModel);

        MessageModel autoMessageModel = new MessageModel("title", "template", MessageType.EVENT,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(autoMessageModel);

        List<MessageCreateDto> manualMessageDtoList = messageService.findMessageList("title", null, null, MessageType.MANUAL, 1, 10);

        List<MessageCreateDto> autoMessageDtoList = messageService.findMessageList("title", null, null, MessageType.EVENT, 1, 10);

        long manualCount = messageService.findMessageCount("title", null, null, MessageType.MANUAL);
        long autoCount = messageService.findMessageCount("title", null, null, MessageType.EVENT);

        assertThat(1, is(manualMessageDtoList.size()));
        assertThat(1, is(autoMessageDtoList.size()));
        assertThat(1L, is(manualCount));
        assertThat(1L, is(autoCount));

    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("email@tuotiansudai.com");
        fakeUser.setMobile(String.valueOf(UUID.randomUUID()).substring(0, 13));
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

    private MessageCreateDto prepareData() {
        UserModel userModel = getFakeUser("testUser");
        UserModel testUserModel = getFakeUser("updateUser");
        userMapper.create(userModel);
        userMapper.create(testUserModel);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), userModel.getLoginName());
        messageMapper.create(messageModel);
        return new MessageCreateDto(messageModel);
    }

    @Test
    public void testCreateAndEditManualMessage() throws Exception {
        MessageCreateDto originMessageCreateDto = prepareData();

        messageService.createAndEditManualMessage(originMessageCreateDto, 0);
        MessageCreateDto messageCreateDto = messageService.getMessageByMessageId(originMessageCreateDto.getId());
        assertEquals(originMessageCreateDto.getId(), messageCreateDto.getId());
        assertEquals(originMessageCreateDto.getTitle(), messageCreateDto.getTitle());
        assertEquals(originMessageCreateDto.getTemplate(), messageCreateDto.getTemplate());
        assertEquals(originMessageCreateDto.getType(), messageCreateDto.getType());
        assertEquals(originMessageCreateDto.getUserGroups(), messageCreateDto.getUserGroups());
        assertEquals(originMessageCreateDto.getChannels(), messageCreateDto.getChannels());
        assertEquals(originMessageCreateDto.getStatus(), messageCreateDto.getStatus());
        assertEquals(originMessageCreateDto.getReadCount(), messageCreateDto.getReadCount());
        assertEquals(originMessageCreateDto.getActivatedBy(), messageCreateDto.getActivatedBy());
        assertEquals(originMessageCreateDto.getActivatedTime(), messageCreateDto.getActivatedTime());
        assertEquals(originMessageCreateDto.getUpdatedBy(), messageCreateDto.getUpdatedBy());
        assertEquals(originMessageCreateDto.getCreatedBy(), messageCreateDto.getCreatedBy());

        originMessageCreateDto.setTitle("editTitle");
        originMessageCreateDto.setTemplate("editTitle");
        originMessageCreateDto.setChannels(Lists.newArrayList(MessageChannel.APP_MESSAGE));
        originMessageCreateDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
        messageService.createAndEditManualMessage(originMessageCreateDto, 0);
        messageCreateDto = messageService.getMessageByMessageId(originMessageCreateDto.getId());
        assertEquals(originMessageCreateDto.getId(), messageCreateDto.getId());
        assertEquals(originMessageCreateDto.getTitle(), messageCreateDto.getTitle());
        assertEquals(originMessageCreateDto.getTemplate(), messageCreateDto.getTemplate());
        assertEquals(originMessageCreateDto.getType(), messageCreateDto.getType());
        assertEquals(originMessageCreateDto.getUserGroups(), messageCreateDto.getUserGroups());
        assertEquals(originMessageCreateDto.getChannels(), messageCreateDto.getChannels());
        assertEquals(originMessageCreateDto.getStatus(), messageCreateDto.getStatus());
        assertEquals(originMessageCreateDto.getReadCount(), messageCreateDto.getReadCount());
        assertEquals(originMessageCreateDto.getActivatedBy(), messageCreateDto.getActivatedBy());
        assertEquals(originMessageCreateDto.getActivatedTime(), messageCreateDto.getActivatedTime());
        assertEquals(originMessageCreateDto.getUpdatedBy(), messageCreateDto.getUpdatedBy());
        assertEquals(originMessageCreateDto.getCreatedBy(), messageCreateDto.getCreatedBy());
    }

    @Test
    public void testMessageExisted() throws Exception {
        MessageCreateDto messageCreateDto = prepareData();

        assertTrue(messageService.isMessageExist(messageCreateDto.getId()));
        assertFalse(messageService.isMessageExist(-1));
    }

    @Test
    public void testGetMessageByMessageId() throws Exception {
        MessageCreateDto originMessageCreateDto = prepareData();

        MessageCreateDto messageCreateDto = messageService.getMessageByMessageId(originMessageCreateDto.getId());

        assertEquals(originMessageCreateDto.getId(), messageCreateDto.getId());
        assertEquals(originMessageCreateDto.getTitle(), messageCreateDto.getTitle());
        assertEquals(originMessageCreateDto.getTemplate(), messageCreateDto.getTemplate());
        assertEquals(originMessageCreateDto.getType(), messageCreateDto.getType());
        assertEquals(originMessageCreateDto.getUserGroups(), messageCreateDto.getUserGroups());
        assertEquals(originMessageCreateDto.getChannels(), messageCreateDto.getChannels());
        assertEquals(originMessageCreateDto.getStatus(), messageCreateDto.getStatus());
        assertEquals(originMessageCreateDto.getReadCount(), messageCreateDto.getReadCount());
        assertEquals(originMessageCreateDto.getActivatedBy(), messageCreateDto.getActivatedBy());
        assertEquals(originMessageCreateDto.getActivatedTime(), messageCreateDto.getActivatedTime());
        assertEquals(originMessageCreateDto.getUpdatedBy(), messageCreateDto.getUpdatedBy());
        assertEquals(originMessageCreateDto.getCreatedBy(), messageCreateDto.getCreatedBy());
    }

    public void testRejectManualMessage() throws Exception {
        MessageCreateDto originMessageCreateDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.rejectManualMessage(originMessageCreateDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageCreateDto messageCreateDto = messageService.getMessageByMessageId(originMessageCreateDto.getId());
        assertEquals(MessageStatus.REJECTION, messageCreateDto.getStatus());
        assertEquals("updateUser", messageCreateDto.getUpdatedBy());
        baseDto = messageService.rejectManualMessage(originMessageCreateDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testApproveManualMessage() throws Exception {
        MessageCreateDto originMessageCreateDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.approveManualMessage(originMessageCreateDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageCreateDto messageCreateDto = messageService.getMessageByMessageId(originMessageCreateDto.getId());
        assertEquals(MessageStatus.APPROVED, messageCreateDto.getStatus());
        assertEquals("updateUser", messageCreateDto.getUpdatedBy());
        baseDto = messageService.rejectManualMessage(originMessageCreateDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testDeleteManualMessage() throws Exception {
        MessageCreateDto messageDto = prepareData();
        messageService.deleteManualMessage(messageDto.getId(), "");
        assertTrue(null == messageService.getMessageByMessageId(messageDto.getId()));
    }
}
