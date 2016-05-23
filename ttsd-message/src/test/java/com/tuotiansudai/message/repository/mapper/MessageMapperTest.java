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
