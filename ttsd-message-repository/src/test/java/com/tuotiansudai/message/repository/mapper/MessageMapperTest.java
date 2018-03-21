package com.tuotiansudai.message.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class MessageMapperTest extends BaseMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void shouldCreateMessage() {
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

        assertNotNull(messageMapper.findById(messageModel.getId()).getId());
    }
}
