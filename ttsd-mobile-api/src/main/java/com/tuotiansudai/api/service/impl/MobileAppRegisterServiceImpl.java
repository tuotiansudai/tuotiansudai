package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRegisterServiceImpl implements MobileAppRegisterService {

    static Logger logger = Logger.getLogger(MobileAppRegisterServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Override
    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp) {
        BaseResponseDto dto = new BaseResponseDto();
        String returnCode = this.verifyMobileNumber(mobileNumber);
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            BaseDto<SmsDataDto> smsDto = smsCaptchaService.sendRegisterCaptcha(mobileNumber, remoteIp);
            if (!smsDto.isSuccess() || !smsDto.getData().getStatus()) {
                returnCode = ReturnMessage.SEND_SMS_IS_FAIL.getCode();
                logger.info(mobileNumber + ":" + ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            }
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
    }

    @Override
    public String verifyMobileNumber(String mobileNumber) {
        if (StringUtils.isEmpty(mobileNumber)) {
            logger.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_NULL.getMsg());
            return ReturnMessage.MOBILE_NUMBER_IS_NULL.getCode();
        }
        if (userService.mobileIsExist(mobileNumber)) {
            logger.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
            return ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
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
