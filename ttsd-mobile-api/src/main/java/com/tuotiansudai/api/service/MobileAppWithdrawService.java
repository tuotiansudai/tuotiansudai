package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.WithdrawOperateRequestDto;

public interface MobileAppWithdrawService {
    BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto);

    BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto);
}
