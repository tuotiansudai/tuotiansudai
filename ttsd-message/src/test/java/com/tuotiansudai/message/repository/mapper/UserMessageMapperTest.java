package com.tuotiansudai.message.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.message.repository.model.*;
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
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(messageModel);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), creator.getLoginName(), messageModel.getTitle(), messageModel.getTemplate());
        userMessageMapper.create(userMessageModel);

        List<UserMessageModel> userMessageModels = userMessageMapper.findByLoginName(creator.getLoginName());
        assertThat(userMessageModels.size(), is(1));

        UserMessageModel actualUserMessageModel = userMessageModels.get(0);
        assertNotNull(actualUserMessageModel.getId());
        assertThat(actualUserMessageModel.getLoginName(), is(userMessageModel.getLoginName()));
        assertThat(actualUserMessageModel.getTitle(), is(userMessageModel.getTitle()));
        assertThat(actualUserMessageModel.getContent(), is(userMessageModel.getContent()));
        assertThat(actualUserMessageModel.isRead(), is(userMessageModel.isRead()));
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
