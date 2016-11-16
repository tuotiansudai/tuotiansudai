package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppUserMessageService {

    BaseResponseDto getUserMessages(UserMessagesRequestDto requestDto);

    BaseResponseDto<MobileAppUnreadMessageCount> getUnreadMessageCount(BaseParamDto baseParamDto);

    BaseResponseDto updateReadMessage(String messageId);

    UserMessageViewDto getUserMessageModelByIdAndLoginName(long userMessageId, String loginName);
}
