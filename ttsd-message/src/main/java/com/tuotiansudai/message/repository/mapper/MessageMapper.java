package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    MessageModel findById(long id);

    void create(MessageModel messageModel);

    void update(MessageModel messageModel);

    long findMessageCount(@Param(value = "title") String title,
                               @Param(value = "messageStatus") MessageStatus messageStatus,
                               @Param(value = "createdBy") String createdBy,
                               @Param(value = "messageType") MessageType messageType);

    List<MessageModel> findMessageList(@Param(value = "title") String title,
                                             @Param(value = "messageStatus") MessageStatus messageStatus,
                                             @Param(value = "createdBy") String createdBy,
                                             @Param(value = "messageType") MessageType messageType,
                                             @Param(value = "index") int index,
                                             @Param(value = "pageSize") int pageSize);

    List<MessageModel> findAssignableManualMessages(String loginName);

}
