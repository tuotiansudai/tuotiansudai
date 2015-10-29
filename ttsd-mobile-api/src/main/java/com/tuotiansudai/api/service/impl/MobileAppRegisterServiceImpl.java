package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRegisterServiceImpl implements MobileAppRegisterService {

    @Override
    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyMobileNumber(String mobileNumber) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyUserName(String userName) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyReferrer(String userName) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyCaptcha(String mobile, String captcha) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyPassword(String password) {
        throw new NotImplementedException(getClass().getName());
    }
}
