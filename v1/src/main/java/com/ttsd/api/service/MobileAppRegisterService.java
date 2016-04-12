package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.RegisterRequestDto;

public interface MobileAppRegisterService {


    BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp);

    String verifyMobileNumber(String mobileNumber);

    String verifyUserName(String userName);

    String verifyReferrer(String userName);

    String verifyCaptcha(String mobile, String captcha);

    BaseResponseDto registerUser(RegisterRequestDto registerRequestDto);

    String verifyPassword(String password);
}
