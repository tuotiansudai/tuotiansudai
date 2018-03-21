package com.tuotiansudai.message.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.message.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class UserMessageMapperTest extends BaseMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Test
    public void shouldCreateUserMessage() {
        MessageModel messageModel = new MessageModel("title",
                "template",
                "messageText",
                MessageUserGroup.ALL_USER,
                MessageCategory.ACTIVITY,
                Lists.newArrayList(MessageChannel.WEBSITE),
                "webUrl",
                AppUrl.HOME,
                null,
                "created",
                DateTime.parse("0001-01-01").toDate(),
                DateTime.parse("9999-12-31").toDate());
        messageMapper.create(messageModel);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(),
                "user",
                messageModel.getTitle(),
                messageModel.getTemplate(),
                new Date());
        userMessageMapper.create(userMessageModel);

        assertNotNull(userMessageMapper.findById(userMessageModel.getId()));
    }
}
