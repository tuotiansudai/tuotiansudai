package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Mock
    private IdGenerator idGenerator;

    @Test
    public void shouldFindMessageList() throws Exception{

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

        List<MessageModel> manualMessageModelList = messageService.findMessageList("title", null, null, MessageType.MANUAL, 1, 10);

        List<MessageModel> autoMessageModelList =messageService.findMessageList("title", null, null, MessageType.EVENT, 1, 10);

        long manualCount = messageService.findMessageCount("title", null, null, MessageType.MANUAL);
        long autoCount = messageService.findMessageCount("title", null, null, MessageType.EVENT);

        assertThat(1, is(manualMessageModelList.size()));
        assertThat(1, is(autoMessageModelList.size()));
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

    private MessageDto prepareData() {
        UserModel userModel = getFakeUser("testUser");
        UserModel testUserModel = getFakeUser("updateUser");
        userMapper.create(userModel);
        userMapper.create(testUserModel);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), userModel.getLoginName());
        messageMapper.create(messageModel);
        return new MessageDto(messageModel);
    }

    @Test
    public void testCreateAndEditManualMessage() throws Exception {
        MessageDto originMessageDto = prepareData();

        messageService.createAndEditManualMessage(originMessageDto, 0);
        MessageDto messageDto = messageService.getMessageByMessageId(originMessageDto.getId());
        assertEquals(originMessageDto.getId(), messageDto.getId());
        assertEquals(originMessageDto.getTitle(), messageDto.getTitle());
        assertEquals(originMessageDto.getTemplate(), messageDto.getTemplate());
        assertEquals(originMessageDto.getType(), messageDto.getType());
        assertEquals(originMessageDto.getUserGroups(), messageDto.getUserGroups());
        assertEquals(originMessageDto.getChannels(), messageDto.getChannels());
        assertEquals(originMessageDto.getStatus(), messageDto.getStatus());
        assertEquals(originMessageDto.getReadCount(), messageDto.getReadCount());
        assertEquals(originMessageDto.getActivatedBy(), messageDto.getActivatedBy());
        assertEquals(originMessageDto.getActivatedTime(), messageDto.getActivatedTime());
        assertEquals(originMessageDto.getUpdatedBy(), messageDto.getUpdatedBy());
        assertEquals(originMessageDto.getCreatedBy(), messageDto.getCreatedBy());

        originMessageDto.setTitle("editTitle");
        originMessageDto.setTemplate("editTitle");
        originMessageDto.setChannels(Lists.newArrayList(MessageChannel.APP_MESSAGE));
        originMessageDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));

        messageService.createAndEditManualMessage(originMessageDto, 0);
        messageDto = messageService.getMessageByMessageId(originMessageDto.getId());
        assertEquals(originMessageDto.getId(), messageDto.getId());
        assertEquals(originMessageDto.getTitle(), messageDto.getTitle());
        assertEquals(originMessageDto.getTemplate(), messageDto.getTemplate());
        assertEquals(originMessageDto.getType(), messageDto.getType());
        assertEquals(originMessageDto.getUserGroups(), messageDto.getUserGroups());
        assertEquals(originMessageDto.getChannels(), messageDto.getChannels());
        assertEquals(originMessageDto.getStatus(), messageDto.getStatus());
        assertEquals(originMessageDto.getReadCount(), messageDto.getReadCount());
        assertEquals(originMessageDto.getActivatedBy(), messageDto.getActivatedBy());
        assertEquals(originMessageDto.getActivatedTime(), messageDto.getActivatedTime());
        assertEquals(originMessageDto.getUpdatedBy(), messageDto.getUpdatedBy());
        assertEquals(originMessageDto.getCreatedBy(), messageDto.getCreatedBy());
    }

    @Test
    public void testMessageExisted() throws Exception {
        MessageDto messageDto = prepareData();

        assertTrue(messageService.isMessageExist(messageDto.getId()));
        assertFalse(messageService.isMessageExist(-1));
    }

    @Test
    public void testGetMessageByMessageId() throws Exception {
        MessageDto originMessageDto = prepareData();

        MessageDto messageDto = messageService.getMessageByMessageId(originMessageDto.getId());

        assertEquals(originMessageDto.getId(), messageDto.getId());
        assertEquals(originMessageDto.getTitle(), messageDto.getTitle());
        assertEquals(originMessageDto.getTemplate(), messageDto.getTemplate());
        assertEquals(originMessageDto.getType(), messageDto.getType());
        assertEquals(originMessageDto.getUserGroups(), messageDto.getUserGroups());
        assertEquals(originMessageDto.getChannels(), messageDto.getChannels());
        assertEquals(originMessageDto.getStatus(), messageDto.getStatus());
        assertEquals(originMessageDto.getReadCount(), messageDto.getReadCount());
        assertEquals(originMessageDto.getActivatedBy(), messageDto.getActivatedBy());
        assertEquals(originMessageDto.getActivatedTime(), messageDto.getActivatedTime());
        assertEquals(originMessageDto.getUpdatedBy(), messageDto.getUpdatedBy());
        assertEquals(originMessageDto.getCreatedBy(), messageDto.getCreatedBy());
    }

    public void testRejectManualMessage() throws Exception {
        MessageDto originMessageDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.rejectManualMessage(originMessageDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageDto messageDto = messageService.getMessageByMessageId(originMessageDto.getId());
        assertEquals(MessageStatus.REJECTION, messageDto.getStatus());
        assertEquals("updateUser", messageDto.getUpdatedBy());
        baseDto = messageService.rejectManualMessage(originMessageDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testApproveManualMessage() throws Exception {
        MessageDto originMessageDto = prepareData();
        BaseDto<BaseDataDto> baseDto = messageService.approveManualMessage(originMessageDto.getId(), "updateUser");
        assertTrue(baseDto.getData().getStatus());
        MessageDto messageDto = messageService.getMessageByMessageId(originMessageDto.getId());
        assertEquals(MessageStatus.APPROVED, messageDto.getStatus());
        assertEquals("updateUser", messageDto.getUpdatedBy());
        baseDto = messageService.rejectManualMessage(originMessageDto.getId(), "");
        assertFalse(baseDto.getData().getStatus());
    }

    public void testDeleteManualMessage() throws Exception {
        MessageDto messageDto = prepareData();
        messageService.deleteManualMessage(messageDto.getId(), "");
        assertTrue(null == messageService.getMessageByMessageId(messageDto.getId()));
    }
}
