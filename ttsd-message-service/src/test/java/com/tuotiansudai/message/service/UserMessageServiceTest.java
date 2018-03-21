package com.tuotiansudai.message.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class UserMessageServiceTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMessageService userMessageService;

    @Test
    public void shouldFindMessageDetail() throws Exception {
        MessageModel webSiteMessageModel = new MessageModel("title",
                "message",
                "messageText",
                MessageUserGroup.ALL_USER,
                MessageCategory.ACTIVITY,
                Lists.newArrayList(MessageChannel.WEBSITE),
                null,
                null,
                null,
                "messageCreator",
                DateTime.parse("0001-01-01").toDate(),
                DateTime.parse("9999-12-31").toDate());

        webSiteMessageModel.setReadCount(10);
        webSiteMessageModel.setActivatedTime(new Date());

        messageMapper.create(webSiteMessageModel);

        UserMessageModel userMessageModel = new UserMessageModel(webSiteMessageModel.getId(),
                "user", webSiteMessageModel.getTitle(), webSiteMessageModel.getTemplate(), webSiteMessageModel.getActivatedTime());

        userMessageMapper.create(userMessageModel);

        UserMessageModel userMessageModel1 = userMessageService.readMessage(userMessageModel.getId());

        assertThat(true, is(userMessageModel1.isRead()));
        assertThat(11L, is(messageMapper.findActiveById(userMessageModel1.getMessageId()).getReadCount()));


    }

    @Test
    public void shouldGetUnreadMessageCount() {
        MessageModel webSiteMessageModel = new MessageModel("title",
                "message",
                "messageText",
                MessageUserGroup.ALL_USER,
                MessageCategory.ACTIVITY,
                Lists.newArrayList(MessageChannel.WEBSITE),
                null,
                null,
                null,
                "messageCreator",
                DateTime.parse("0001-01-01").toDate(),
                DateTime.parse("9999-12-31").toDate());

        messageMapper.create(webSiteMessageModel);

        MessageModel appMessageModel = new MessageModel("app title",
                "message",
                "messageText",
                MessageUserGroup.ALL_USER,
                MessageCategory.ACTIVITY,
                Lists.newArrayList(MessageChannel.APP_MESSAGE),
                null,
                null,
                null,
                "messageCreator",
                DateTime.parse("0001-01-01").toDate(),
                DateTime.parse("9999-12-31").toDate());

        messageMapper.create(appMessageModel);
    }
}
