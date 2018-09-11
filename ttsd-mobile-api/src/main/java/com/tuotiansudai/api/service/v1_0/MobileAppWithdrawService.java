package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppWithdrawService {
    BaseResponseDto<WithdrawListResponseDataDto> queryUserWithdrawLogs(WithdrawListRequestDto requestDto);

    BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto);
}
