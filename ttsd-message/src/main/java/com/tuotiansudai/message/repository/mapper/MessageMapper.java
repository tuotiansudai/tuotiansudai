package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.MessageEventType;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageMapper {

    MessageModel findById(long id);

    MessageModel lockById(long id);

    MessageModel findActiveByEventType(@Param(value = "eventType") MessageEventType eventType);

    void create(MessageModel messageModel);

    void update(MessageModel messageModel);

    void deleteById(@Param(value = "messageId") long messageId,
                    @Param(value = "updatedBy") String updatedBy,
                    @Param(value = "updatedTime") Date updatedTime);

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

    List<MessageModel> findAssignableMessages(String loginName);

}
