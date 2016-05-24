package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.jpush.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageModel;


public interface MessageService {

    BasePaginationDataDto<MessageModel> findMessagePaginationList(String title, MessageStatus messageStatus, String createBy, int index, int pageSize);

    long findMessagePaginationCount(String title, MessageStatus messageStatus, String createBy);

}
