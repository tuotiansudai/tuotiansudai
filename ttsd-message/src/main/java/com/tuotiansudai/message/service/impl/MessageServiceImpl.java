package com.tuotiansudai.message.service.impl;

import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public List<MessageModel> findMessageList(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        return messageMapper.findMessageList(title, messageStatus, createdBy, messageType, (index-1)*pageSize, pageSize);
    }

    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType){
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }

}
