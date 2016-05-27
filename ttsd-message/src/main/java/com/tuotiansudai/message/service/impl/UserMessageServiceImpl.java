package com.tuotiansudai.message.service.impl;

import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserMessageServiceImpl  implements UserMessageService{

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional
    public UserMessageModel findById(long id) {
        UserMessageModel userMessageModel = userMessageMapper.findById(id);
        userMessageModel.setRead(true);
        userMessageModel.setReadTime(new Date());
        userMessageMapper.update(userMessageModel);

        MessageModel messageModel = messageMapper.findById(userMessageModel.getMessageId());
        messageModel.setReadCount(messageModel.getReadCount() + 1);
        messageMapper.update(messageModel);
        return userMessageModel;
    }
}
