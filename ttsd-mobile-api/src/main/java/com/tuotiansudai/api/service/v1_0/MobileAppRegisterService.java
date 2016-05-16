package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RegisterRequestDto;

public interface MobileAppRegisterService {

    BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp);

    BaseResponseDto registerUser(RegisterRequestDto registerRequestDto);


}
