package com.tuotiansudai.api.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppRetrievePasswordService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRetrievePasswordServiceImpl implements MobileAppRetrievePasswordService {

    static Logger log = Logger.getLogger(MobileAppRegisterServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Override
    public BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto validateAuthCode(RetrievePasswordRequestDto retrievePasswordRequestDto) {

        BaseResponseDto dto = new BaseResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        if (Strings.isNullOrEmpty(authCode)) {
            //验证码不能为空
            dto.setCode(ReturnMessage.SMS_CAPTCHA_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_IS_NULL.getMsg());
            return dto;
        }
        boolean verified = smsCaptchaService.verifyMobileCaptcha(phoneNumber, authCode, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        if (verified) {
            //验证码输入正确
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            //验证码输入错误
            dto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
        return dto;
    }

    @Override
    public BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto, String remoteIp) {
        BaseResponseDto dto = new BaseResponseDto();
        String authType = retrievePasswordRequestDto.getAuthType();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        if (Strings.isNullOrEmpty(authType)) {
            //验证码类型不能为空
            dto.setCode(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getMsg());
            return dto;
        }
        if (!userService.mobileIsExist(phoneNumber)) {
            dto.setCode(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getCode());
            dto.setMessage(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getMsg());
            return dto;
        }

        BaseDto<SmsDataDto> smsDto = smsCaptchaService.sendRetrievePasswordCaptcha(phoneNumber, remoteIp);
        if (smsDto.isSuccess() && smsDto.getData().getStatus()) {
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            log.error("send retrieve password message fail , cause by message interface fault !");
            dto.setCode(ReturnMessage.SEND_SMS_IS_FAIL.getCode());
            dto.setMessage(ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
        }
        return dto;
    }
}
