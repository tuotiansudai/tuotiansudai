package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCompleteDto;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MessageService {

    List<MessageCompleteDto> findMessageCompleteDtoList(String title, MessageStatus messageStatus, String createBy, MessageType messageType, int index, int pageSize);

    long findMessageCount(String title, MessageStatus messageStatus, String createBy, MessageType messageType);

    long createAndEditManualMessage(MessageCompleteDto messageCompleteDto, long importUsersId);

    long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException;

    boolean isMessageExist(long messageId);

    MessageCompleteDto findMessageCompleteDtoByMessageId(long messageId);

    BaseDto<BaseDataDto> rejectMessage(long messageId, String checkerName);

    BaseDto<BaseDataDto> approveMessage(long messageId, String checkerName);

    BaseDto<BaseDataDto> deleteMessage(long messageId, String updatedBy);
}
