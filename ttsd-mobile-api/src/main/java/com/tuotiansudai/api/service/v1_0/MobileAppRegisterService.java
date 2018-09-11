package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppRegisterService {

    BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp);

    BaseResponseDto<RegisterResponseDataDto> registerUser(RegisterRequestDto registerRequestDto);

    BaseResponseDto mobileNumberIsExist(MobileIsAvailableRequestDto requestDto);
}
