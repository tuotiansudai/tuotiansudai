package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.RegisterRequestDto;
import com.ttsd.api.dto.RegisterResponseDto;

public interface MobileRegisterAppService {


    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber);

    public String verifyMobileNumber(String mobileNumber);

    public String verifyUserName(String userName);

    public String verifyReferrer(String userName);

    public String verifyCaptcha(String mobile,String captcha);

    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto);

    public String verifyPassword(String password);
}
