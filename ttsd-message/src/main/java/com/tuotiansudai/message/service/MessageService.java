package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.MessageDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MessageService {

    List<MessageModel> findMessageList(String title, MessageStatus messageStatus, String createBy, MessageType messageType, int index, int pageSize);

    long findMessageCount(String title, MessageStatus messageStatus, String createBy, MessageType messageType);

    BasePaginationDataDto<UserMessagePaginationItemDto> getUserMessages(String loginName, int index, int pageSize);

    void createAndEditManualMessage(MessageDto messageDto, long importUsersId);

    long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException;

    MessageDto getMessageByMessageId(long messageId);

    BasePaginationDataDto<MessageDto> getManualMessageList(String title, MessageStatus messageStatus, String creator, int index, int pageSize);

    BaseDto<BaseDataDto> rejectManualMessage(long messageId);

    BaseDto<BaseDataDto> approveManualMessage(long messageId);

    BaseDto<BaseDataDto> deleteManualMessage(long messageId);

    long getMessageReceiverCount(long messageId);
}
