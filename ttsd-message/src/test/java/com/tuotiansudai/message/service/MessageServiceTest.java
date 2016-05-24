package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    public void shouldFindMessageList() throws Exception{

        UserModel creator = getFakeUser("messageCreator");
        userMapper.create(creator);

        MessageModel manualMessageModel = new MessageModel("title", "template", MessageType.MANUAL,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
                Lists.newArrayList(MessageChannel.WEBSITE),
                MessageStatus.TO_APPROVE, new Date(), creator.getLoginName());
        messageMapper.create(manualMessageModel);

        MessageModel autoMessageModel = new MessageModel("title", "template", MessageType.EVENT,
                Lists.newArrayList(UserGroup.ALL_USER, UserGroup.STAFF),
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
        fakeUser.setMobile("11900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUser;
    }

}
