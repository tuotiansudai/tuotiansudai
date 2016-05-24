package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.MessageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    MessageModel findById(long id);

    void create(MessageModel messageModel);

    void update(MessageModel messageModel);

    List<MessageModel> findAssignableManualMessages(String loginName);
}
