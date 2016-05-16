package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.v1_0.WithdrawOperateRequestDto;

public interface MobileAppWithdrawService {
    BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto);

    BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto);
}
