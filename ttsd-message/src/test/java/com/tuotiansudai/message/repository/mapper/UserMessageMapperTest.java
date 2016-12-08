package com.tuotiansudai.message.repository.mapper;

import com.google.common.collect.Lists;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMessageMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Test
    public void shouldCreateUserMessage() {
        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageModel.setEventType(MessageEventType.INVEST_SUCCESS);
        messageMapper.create(messageModel);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), creator.getLoginName(), messageModel.getTitle(), messageModel.getTitle(), messageModel.getTemplate(), messageModel.getActivatedTime());
        userMessageModel.setBusinessId("111");
        userMessageMapper.create(userMessageModel);

        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(creator.getLoginName(), null,null, null);
        assertThat(userMessageModels.size(), is(1));

        UserMessageModel actualUserMessageModel = userMessageModels.get(0);
        assertNotNull(actualUserMessageModel.getId());
        assertThat(actualUserMessageModel.getLoginName(), is(userMessageModel.getLoginName()));
        assertThat(actualUserMessageModel.getTitle(), is(userMessageModel.getTitle()));
        assertThat(actualUserMessageModel.getContent(), is(userMessageModel.getContent()));
        assertThat(actualUserMessageModel.isRead(), is(userMessageModel.isRead()));

        UserMessageModel model = userMessageMapper.findOneMessage(creator.getLoginName(), "111", MessageEventType.INVEST_SUCCESS);
        assertNotNull(model);
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

    @Test
    public void shouldCountMessagesByLoginNameAndMessageTypeISok(){
        UserModel userModel = getFakeUser("testUserMessage");
        userMapper.create(userModel);

        MessageModel messageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(MessageUserGroup.ALL_USER),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), userModel.getLoginName());
        messageModel.setEventType(MessageEventType.LOAN_OUT_SUCCESS);
        messageMapper.create(messageModel);

        long l = userMessageMapper.countMessagesByLoginNameAndMessageType(userModel.getLoginName(), messageModel.getId(), messageModel.getTitle());
        assertTrue(l == 0);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), userModel.getLoginName(), messageModel.getTitle(), messageModel.getTitle(), messageModel.getTemplate(), messageModel.getActivatedTime());
        userMessageMapper.create(userMessageModel);

        l = userMessageMapper.countMessagesByLoginNameAndMessageType(userModel.getLoginName(), messageModel.getId(), messageModel.getTitle());
        assertTrue(l == 1);
    }

}
