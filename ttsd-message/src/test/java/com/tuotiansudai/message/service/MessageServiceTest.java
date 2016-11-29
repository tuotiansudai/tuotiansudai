package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCompleteDto;
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

        List<MessageCompleteDto> manualMessageDtoList = messageService.findMessageCompleteDtoList("title", null, null, MessageType.MANUAL, 1, 10);

        List<MessageCompleteDto> autoMessageDtoList = messageService.findMessageCompleteDtoList("title", null, null, MessageType.EVENT, 1, 10);

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

    private MessageCompleteDto prepareData() {
        UserModel userModel = getFakeUser("testUser");
        UserModel testUserModel = getFakeUser("updateUser");
        userMapper.create(userModel);
        userMapper.create(testUserModel);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), userModel.getLoginName());
        messageMapper.create(messageModel);
        return new MessageCompleteDto(messageModel);
    }

    @Test
    public void testCreateAndEditManualMessage() throws Exception {
        MessageCompleteDto originMessageCompleteDto = prepareData();

        messageService.createAndEditManualMessage(originMessageCompleteDto, 0);
        MessageCompleteDto messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(originMessageCompleteDto.getId());
        assertEquals(originMessageCompleteDto.getId(), messageCompleteDto.getId());
        assertEquals(originMessageCompleteDto.getTitle(), messageCompleteDto.getTitle());
        assertEquals(originMessageCompleteDto.getTemplate(), messageCompleteDto.getTemplate());
        assertEquals(originMessageCompleteDto.getType(), messageCompleteDto.getType());
        assertEquals(originMessageCompleteDto.getUserGroups(), messageCompleteDto.getUserGroups());
        assertEquals(originMessageCompleteDto.getChannels(), messageCompleteDto.getChannels());
        assertEquals(originMessageCompleteDto.getStatus(), messageCompleteDto.getStatus());
        assertEquals(originMessageCompleteDto.getReadCount(), messageCompleteDto.getReadCount());
        assertEquals(originMessageCompleteDto.getActivatedBy(), messageCompleteDto.getActivatedBy());
        assertEquals(originMessageCompleteDto.getActivatedTime(), messageCompleteDto.getActivatedTime());
        assertEquals(originMessageCompleteDto.getUpdatedBy(), messageCompleteDto.getUpdatedBy());
        assertEquals(originMessageCompleteDto.getCreatedBy(), messageCompleteDto.getCreatedBy());

        originMessageCompleteDto.setTitle("editTitle");
        originMessageCompleteDto.setTemplate("editTitle");
        originMessageCompleteDto.setChannels(Lists.newArrayList(MessageChannel.APP_MESSAGE));
        originMessageCompleteDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
        messageService.createAndEditManualMessage(originMessageCompleteDto, 0);
        messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(originMessageCompleteDto.getId());
        assertEquals(originMessageCompleteDto.getId(), messageCompleteDto.getId());
        assertEquals(originMessageCompleteDto.getTitle(), messageCompleteDto.getTitle());
        assertEquals(originMessageCompleteDto.getTemplate(), messageCompleteDto.getTemplate());
        assertEquals(originMessageCompleteDto.getType(), messageCompleteDto.getType());
        assertEquals(originMessageCompleteDto.getUserGroups(), messageCompleteDto.getUserGroups());
        assertEquals(originMessageCompleteDto.getChannels(), messageCompleteDto.getChannels());
        assertEquals(originMessageCompleteDto.getStatus(), messageCompleteDto.getStatus());
        assertEquals(originMessageCompleteDto.getReadCount(), messageCompleteDto.getReadCount());
        assertEquals(originMessageCompleteDto.getActivatedBy(), messageCompleteDto.getActivatedBy());
        assertEquals(originMessageCompleteDto.getActivatedTime(), messageCompleteDto.getActivatedTime());
        assertEquals(originMessageCompleteDto.getUpdatedBy(), messageCompleteDto.getUpdatedBy());
        assertEquals(originMessageCompleteDto.getCreatedBy(), messageCompleteDto.getCreatedBy());
    }

    @Test
    public void testMessageExisted() throws Exception {
        MessageCompleteDto messageCompleteDto = prepareData();

        assertTrue(messageService.isMessageExist(messageCompleteDto.getId()));
        assertFalse(messageService.isMessageExist(-1));
    }

    @Test
    public void testGetMessageByMessageId() throws Exception {
        MessageCompleteDto originMessageCompleteDto = prepareData();

        MessageCompleteDto messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(originMessageCompleteDto.getId());

        assertEquals(originMessageCompleteDto.getId(), messageCompleteDto.getId());
        assertEquals(originMessageCompleteDto.getTitle(), messageCompleteDto.getTitle());
        assertEquals(originMessageCompleteDto.getTemplate(), messageCompleteDto.getTemplate());
        assertEquals(originMessageCompleteDto.getType(), messageCompleteDto.getType());
        assertEquals(originMessageCompleteDto.getUserGroups(), messageCompleteDto.getUserGroups());
        assertEquals(originMessageCompleteDto.getChannels(), messageCompleteDto.getChannels());
        assertEquals(originMessageCompleteDto.getStatus(), messageCompleteDto.getStatus());
        assertEquals(originMessageCompleteDto.getReadCount(), messageCompleteDto.getReadCount());
        assertEquals(originMessageCompleteDto.getActivatedBy(), messageCompleteDto.getActivatedBy());
        assertEquals(originMessageCompleteDto.getActivatedTime(), messageCompleteDto.getActivatedTime());
        assertEquals(originMessageCompleteDto.getUpdatedBy(), messageCompleteDto.getUpdatedBy());
        assertEquals(originMessageCompleteDto.getCreatedBy(), messageCompleteDto.getCreatedBy());
    }

    public void testRejectManualMessage() throws Exception {
        MessageCompleteDto originMessageCompleteDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.rejectMessage(originMessageCompleteDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageCompleteDto messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(originMessageCompleteDto.getId());
        assertEquals(MessageStatus.REJECTION, messageCompleteDto.getStatus());
        assertEquals("updateUser", messageCompleteDto.getUpdatedBy());
        baseDto = messageService.rejectMessage(originMessageCompleteDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testApproveManualMessage() throws Exception {
        MessageCompleteDto originMessageCompleteDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.approveMessage(originMessageCompleteDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageCompleteDto messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(originMessageCompleteDto.getId());
        assertEquals(MessageStatus.APPROVED, messageCompleteDto.getStatus());
        assertEquals("updateUser", messageCompleteDto.getUpdatedBy());
        baseDto = messageService.rejectMessage(originMessageCompleteDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testDeleteManualMessage() throws Exception {
        MessageCompleteDto messageDto = prepareData();
        messageService.deleteMessage(messageDto.getId(), "");
        assertTrue(null == messageService.findMessageCompleteDtoByMessageId(messageDto.getId()));
    }
}
