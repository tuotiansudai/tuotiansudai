package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.WithdrawListRequestDto;
import com.tuotiansudai.api.dto.WithdrawOperateRequestDto;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    @Override
    public BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
