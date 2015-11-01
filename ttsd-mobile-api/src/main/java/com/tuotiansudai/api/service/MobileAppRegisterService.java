package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;

public interface MobileAppRegisterService {

    BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp);

    BaseResponseDto registerUser(RegisterRequestDto registerRequestDto);


}
