package com.tuotiansudai.api.service.v2_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.SendSmsCompositeRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppSendSmsV2Service;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.CaptchaHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MobileAppSendSmsV2ServiceImpl implements MobileAppSendSmsV2Service {

    static Logger logger = Logger.getLogger(MobileAppSendSmsV2ServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Override
    public BaseResponseDto sendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto, String remoteIp) {
        if (!sendSmsCompositeRequestDto.getType().equals(CaptchaType.NO_PASSWORD_INVEST) && !sendSmsCompositeRequestDto.getType().equals(CaptchaType.TURN_OFF_NO_PASSWORD_INVEST)
                && !captchaHelper.captchaVerify(sendSmsCompositeRequestDto.getImageCaptcha(), sendSmsCompositeRequestDto.getBaseParam().getDeviceId(), remoteIp)) {
            return new BaseResponseDto(ReturnMessage.IMAGE_CAPTCHA_IS_WRONG.getCode(), ReturnMessage.IMAGE_CAPTCHA_IS_WRONG.getMsg());
        }

        ReturnMessage returnMessage = checkSendSms(sendSmsCompositeRequestDto);
        if (!returnMessage.equals(ReturnMessage.SUCCESS)) {
            return new BaseResponseDto(returnMessage.getCode(), returnMessage.getMsg());
        }
        BaseResponseDto dto = new BaseResponseDto();
        BaseDto<SmsDataDto> smsDto = null;

        switch (sendSmsCompositeRequestDto.getType()) {
            case REGISTER_CAPTCHA:
                smsDto = smsCaptchaService.sendRegisterCaptcha(sendSmsCompositeRequestDto.getPhoneNum(), remoteIp);
                break;
            case RETRIEVE_PASSWORD_CAPTCHA:
                smsDto = smsCaptchaService.sendRetrievePasswordCaptcha(sendSmsCompositeRequestDto.getPhoneNum(), remoteIp);
                break;
            case NO_PASSWORD_INVEST:
            case TURN_OFF_NO_PASSWORD_INVEST:
                smsDto = smsCaptchaService.sendNoPasswordInvestCaptcha(sendSmsCompositeRequestDto.getPhoneNum(), remoteIp);
                break;
        }

        if (smsDto.isSuccess() && smsDto.getData().getStatus()) {
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            logger.error(MessageFormat.format("{0} send sms fail, error message {1} ", sendSmsCompositeRequestDto.getType().name(), smsDto.getData().getMessage()));
            dto.setCode(ReturnMessage.SEND_SMS_IS_FAIL.getCode());
            dto.setMessage(ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
        }

        return dto;
    }

    private ReturnMessage checkSendSms(SendSmsCompositeRequestDto sendSmsCompositeRequestDto) {

        boolean mobileIsExist = userService.mobileIsExist(sendSmsCompositeRequestDto.getPhoneNum());
        switch (sendSmsCompositeRequestDto.getType()) {
            case REGISTER_CAPTCHA:
                if (mobileIsExist) {
                    return ReturnMessage.MOBILE_NUMBER_IS_EXIST;
                }
                break;
            default:
                if (!mobileIsExist) {
                    return ReturnMessage.MOBILE_NUMBER_NOT_EXIST;
                }
        }
        return ReturnMessage.SUCCESS;
    }
}
