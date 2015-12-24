package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;

public interface MobileAppChangePasswordService {
    BaseResponseDto changePassword(ChangePasswordRequestDto requestDto);
}
