package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.dto.MessagePaginationItemDto;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MessageService {

    List<MessagePaginationItemDto> findMessagePagination(String title, MessageStatus messageStatus, String createBy, MessageType messageType, int index, int pageSize);

    long findMessageCount(String title, MessageStatus messageStatus, String createBy, MessageType messageType);

    long createOrUpdateManualMessage(String loginName, MessageCreateDto messageCreateDto);

    long createImportUsers(InputStream inputStream) throws IOException;

    BaseDto<BaseDataDto> rejectMessage(long messageId, String rejectedBy);

    BaseDto<BaseDataDto> approveMessage(long messageId, String approvedBy);

    BaseDto<BaseDataDto> deleteMessage(long messageId, String deletedBy);

    MessageModel findById(long messageId);

    MessageCreateDto getEditMessage(long messageId);
}
