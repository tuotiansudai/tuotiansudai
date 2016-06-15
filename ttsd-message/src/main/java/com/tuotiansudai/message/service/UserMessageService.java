package com.tuotiansudai.message.service;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.model.UserMessageModel;

public interface UserMessageService {

    int getUnreadMessageCount(String loginName);

    BasePaginationDataDto<UserMessagePaginationItemDto> getUserMessages(String loginName, int index, int pageSize);

    UserMessageModel readMessage(long userMessageId);
}
