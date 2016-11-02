package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MessageService {

    List<MessageCreateDto> findMessageList(String title, MessageStatus messageStatus, String createBy, MessageType messageType, int index, int pageSize);

    long findMessageCount(String title, MessageStatus messageStatus, String createBy, MessageType messageType);

    long createAndEditManualMessage(MessageCreateDto messageCreateDto, long importUsersId);

    long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException;

    boolean isMessageExist(long messageId);

    MessageCreateDto getMessageByMessageId(long messageId);

    BaseDto<BaseDataDto> rejectManualMessage(long messageId, String checkerName);

    BaseDto<BaseDataDto> approveManualMessage(long messageId, String checkerName);

    BaseDto<BaseDataDto> deleteManualMessage(long messageId, String updatedBy);
}
