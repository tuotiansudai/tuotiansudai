package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.WithdrawListRequestDto;
import com.ttsd.api.dto.WithdrawOperateRequestDto;

public interface MobileAppWithdrawService {
    BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto);
    BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto);
}
