package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    void create(MessageModel messageModel);

    void update(MessageModel messageModel);

    void deleteById(@Param(value = "id") long id,
                    @Param(value = "updatedBy") String updatedBy);

    MessageModel findById(long id);

    MessageModel findActiveById(long id);

    MessageModel lockById(long id);

    MessageModel findActiveByEventType(@Param(value = "eventType") MessageEventType eventType);

    long findMessageCount(@Param(value = "title") String title,
                          @Param(value = "messageStatus") MessageStatus messageStatus,
                          @Param(value = "updatedBy") String updatedBy,
                          @Param(value = "messageType") MessageType messageType,
                          @Param(value = "messageCategory") MessageCategory messageCategory);

    List<MessageModel> findMessagePagination(@Param(value = "title") String title,
                                             @Param(value = "messageStatus") MessageStatus messageStatus,
                                             @Param(value = "updatedBy") String updatedBy,
                                             @Param(value = "messageType") MessageType messageType,
                                             @Param(value = "messageCategory") MessageCategory messageCategory,
                                             @Param(value = "index") int index,
                                             @Param(value = "pageSize") int pageSize);

    List<MessageModel> findAssignableManualMessages(String loginName);
}
