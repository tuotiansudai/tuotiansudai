package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;

import javax.servlet.http.HttpServletRequest;

public interface MobileAppChangePasswordService {
    BaseResponseDto changePassword(ChangePasswordRequestDto requestDto, String ip);
}
