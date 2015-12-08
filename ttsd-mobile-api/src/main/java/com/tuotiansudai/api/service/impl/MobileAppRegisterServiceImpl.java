package com.tuotiansudai.api.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.dto.RegisterResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppChannelService;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRegisterServiceImpl implements MobileAppRegisterService {

    static Logger log = Logger.getLogger(MobileAppRegisterServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Override
    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber, String remoteIp) {
        BaseResponseDto dto = new BaseResponseDto();

        String returnCode = ReturnMessage.SUCCESS.getCode();
        if (StringUtils.isEmpty(mobileNumber)) {
            log.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_NULL.getMsg());
            returnCode = ReturnMessage.MOBILE_NUMBER_IS_NULL.getCode();
        }
        if (userService.mobileIsExist(mobileNumber)) {
            log.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
            returnCode = ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            BaseDto<SmsDataDto> smsDto = smsCaptchaService.sendRegisterCaptcha(mobileNumber, remoteIp);
            if (!smsDto.isSuccess() || !smsDto.getData().getStatus()) {
                returnCode = ReturnMessage.SEND_SMS_IS_FAIL.getCode();
                log.info(mobileNumber + ":" + ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            }
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;


    }

    public BaseResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        RegisterUserDto dto = registerRequestDto.convertToRegisterUserDto();

        dto.setChannel(mobileAppChannelService.obtainChannelBySource(registerRequestDto.getBaseParam()));

        if(StringUtils.isEmpty(dto.getLoginName())){
            return new BaseResponseDto(ReturnMessage.USER_NAME_IS_NULL.getCode(),ReturnMessage.USER_NAME_IS_NULL.getMsg());
        }
        boolean loginNameIsExist = userService.loginNameIsExist(dto.getLoginName().toLowerCase());
        if(loginNameIsExist){
            return new BaseResponseDto(ReturnMessage.USER_NAME_IS_EXIST.getCode(),ReturnMessage.USER_NAME_IS_EXIST.getMsg());
        }
        if (StringUtils.isEmpty(dto.getMobile())){
            return new BaseResponseDto(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(),ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
        }
        boolean mobileIsExist = userService.mobileIsExist(dto.getMobile());
        if (mobileIsExist){
            return new BaseResponseDto(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(),ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
        }

        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !userService.loginNameIsExist(dto.getReferrer());
        if (referrerIsNotExist){
            return new BaseResponseDto(ReturnMessage.REFERRER_IS_NOT_EXIST.getCode(),ReturnMessage.REFERRER_IS_NOT_EXIST.getMsg());
        }
        boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);
        if (verifyCaptchaFailed){
            return new BaseResponseDto(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }

        try {
            userService.registerUser(dto);
        } catch (ReferrerRelationException e) {
            return new BaseResponseDto(ReturnMessage.REFERRER_IS_NOT_EXIST.getCode(), e.getMessage());
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
        RegisterResponseDataDto registerDataDto = new RegisterResponseDataDto();
        registerDataDto.setUserId(registerRequestDto.getUserName());
        registerDataDto.setUserName(registerRequestDto.getUserName());
        registerDataDto.setPhoneNum(registerRequestDto.getPhoneNum());
        baseResponseDto.setData(registerDataDto);

        return baseResponseDto;

    }

}
