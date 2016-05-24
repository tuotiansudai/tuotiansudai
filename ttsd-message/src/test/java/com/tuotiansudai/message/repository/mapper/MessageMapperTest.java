package com.tuotiansudai.message.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MessageMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void shouldCreateMessage() {
        UserModel creator = getFakeUser("messageCreator");

        userMapper.create(creator);
        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());

        messageMapper.create(messageModel);

        MessageModel actualMessageModel = messageMapper.findById(messageModel.getId());

        assertNotNull(actualMessageModel.getId());
        assertThat(actualMessageModel.getTitle(), is(messageModel.getTitle()));
        assertThat(actualMessageModel.getTemplate(), is(messageModel.getTemplate()));
        assertThat(actualMessageModel.getType(), is(messageModel.getType()));
        assertTrue(CollectionUtils.isEqualCollection(actualMessageModel.getUserGroups(), messageModel.getUserGroups()));
        assertTrue(CollectionUtils.isEqualCollection(actualMessageModel.getChannels(), messageModel.getChannels()));
        assertThat(actualMessageModel.getStatus(), is(messageModel.getStatus()));
        assertThat(actualMessageModel.getCreatedBy(), is(messageModel.getCreatedBy()));
        assertNotNull(actualMessageModel.getCreatedTime());
        assertThat(actualMessageModel.getUpdatedBy(), is(messageModel.getUpdatedBy()));
        assertNotNull(actualMessageModel.getUpdatedTime());
    }

    @Test
    public void shouldFindMessageList(){

        UserModel creator = getFakeUser("messageCreate");
        userMapper.create(creator);

        MessageModel messageModelManual = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(messageModelManual);

        MessageModel messageModelAuto= new MessageModel("title", "template", MessageType.EVENT,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(messageModelAuto);

        List<MessageModel> manualMessageModelList = messageMapper.findMessageList("title", null, null, MessageType.EVENT, 0, 10);
        List<MessageModel> autoMessageModelList = messageMapper.findMessageList("title", null, null, MessageType.MANUAL, 0, 10);
        long manualMessageCount = messageMapper.findMessageCount("title", null, null, MessageType.MANUAL);
        long autoMessageCount = messageMapper.findMessageCount("title", null, null, MessageType.EVENT);

        assertEquals(1, manualMessageModelList.size());
        assertEquals(1, autoMessageModelList.size());
        assertEquals(1, manualMessageCount);
        assertEquals(1, autoMessageCount);

    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("email@tuotiansudai.com");
        fakeUser.setMobile("11900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }
}
