package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppRegisterService;
import com.tuotiansudai.client.HTrackingClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.mapper.HTrackingUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MobileAppRegisterServiceImpl implements MobileAppRegisterService {

    static Logger log = Logger.getLogger(MobileAppRegisterServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private HTrackingUserMapper hTrackingUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HTrackingClient hTrackingClient;

    private final static String HTRACKING_CHANNEL = "htracking";

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

            if (!smsDto.getData().getStatus() && smsDto.getData().getIsRestricted()) {
                returnCode = ReturnMessage.SMS_IP_RESTRICTED.getCode();
                log.info(mobileNumber + ":" + ReturnMessage.SMS_IP_RESTRICTED.getMsg());
            }
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
    }

    @Override
    public BaseResponseDto<RegisterResponseDataDto> registerUser(RegisterRequestDto registerRequestDto) {
        RegisterUserDto dto = registerRequestDto.convertToRegisterUserDto();

        dto.setChannel(mobileAppChannelService.obtainChannelBySource(registerRequestDto.getBaseParam()));

        boolean loginNameIsExist = false;
        if (StringUtils.isNotEmpty(dto.getLoginName())) {
            loginNameIsExist = userService.loginNameIsExist(dto.getLoginName().toLowerCase());
        }
        if (loginNameIsExist) {
            return new BaseResponseDto(ReturnMessage.USER_NAME_IS_EXIST.getCode(), ReturnMessage.USER_NAME_IS_EXIST.getMsg());
        }
        if (StringUtils.isEmpty(dto.getMobile())) {
            return new BaseResponseDto(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(), ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
        }
        boolean mobileIsExist = userService.mobileIsExist(dto.getMobile());
        if (mobileIsExist) {
            return new BaseResponseDto(ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode(), ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
        }

        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !userService.loginNameOrMobileIsExist(dto.getReferrer());
        if (referrerIsNotExist) {
            return new BaseResponseDto(ReturnMessage.REFERRER_IS_NOT_EXIST.getCode(), ReturnMessage.REFERRER_IS_NOT_EXIST.getMsg());
        }
        boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);
        if (verifyCaptchaFailed) {
            return new BaseResponseDto(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(), ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }

        boolean result = userService.registerUser(dto);
        if (result && dto.getSource() == Source.IOS) {
            this.hTrackingRegister(dto.getMobile(), registerRequestDto.getBaseParam().getDeviceId());
        }

        BaseResponseDto<RegisterResponseDataDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        RegisterResponseDataDto registerDataDto = new RegisterResponseDataDto();
        registerDataDto.setPhoneNum(registerRequestDto.getPhoneNum());
        registerDataDto.setToken(myAuthenticationUtil.createAuthentication(dto.getLoginName(), Source.valueOf(registerRequestDto.getBaseParam().getPlatform().toUpperCase())));
        baseResponseDto.setData(registerDataDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto mobileNumberIsExist(MobileIsAvailableRequestDto requestDto) {
        boolean mobileIsExist = userService.mobileIsExist(requestDto.getMobile());
        return mobileIsExist ? new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg()) :
                new BaseResponseDto(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getCode(), ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getMsg());
    }

    private void hTrackingRegister(String mobile, String deviceId) {
        if (hTrackingUserMapper.findByMobileAndDeviceId(mobile, deviceId) != null) {
            UserModel userModel = userMapper.findByLoginNameOrMobile(mobile);
            userModel.setChannel(HTRACKING_CHANNEL);
            userMapper.updateUser(userModel);
            log.info(MessageFormat.format("[mobile register] send hTrackingRegister, loginName:{0}, deviceId:{1}", userModel.getLoginName(), deviceId));
            hTrackingClient.hTrackingRegister(userModel.getMobile(), deviceId);
        }
    }

}
