package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
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

import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMessageServiceTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageService userMessageService;

    @Test
    public void shouldFindMessageDetail() throws Exception{

        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel messageModel = new MessageModel("title", "hello message!", MessageType.MANUAL,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.APPROVED, new Date(), creator.getLoginName());
        messageModel.setReadCount(10);

        messageMapper.create(messageModel);


        UserModel userTest = getFakeUserTest("userTest");
        userMapper.create(userTest);
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), userTest.getLoginName(), messageModel.getTitle(), messageModel.getTemplate());

        userMessageMapper.create(userMessageModel);

        UserMessageModel userMessageModel1 = userMessageService.findById(userMessageModel.getId());

        assertThat(true, is(userMessageModel1.isRead()));
        assertThat(11L, is(messageMapper.findById(userMessageModel1.getMessageId()).getReadCount()));



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

    private UserModel getFakeUserTest(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("userpassword");
        fakeUser.setEmail("useremail@tuotiansudai.com");
        fakeUser.setMobile("13900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

}
