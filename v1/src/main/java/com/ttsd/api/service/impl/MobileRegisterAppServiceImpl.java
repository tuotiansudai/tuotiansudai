package com.ttsd.api.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.common.service.ValidationService;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileRegisterAppService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@Service
public class MobileRegisterAppServiceImpl implements MobileRegisterAppService {

    @Logger
    static Log log;

    @Resource
    private UserBO userBO;

    @Resource
    private UserService userService;

    @Resource
    ValidationService vdtService;

    @Resource
    private AuthService authService;


    @Override
    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber,String remoteIp) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        returnCode = this.verifyMobileNumber(mobileNumber);
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, CommonConstants.MOBILE_AUTH_MESSAGE_VALID_TIME);
            Long validTime = calendar.getTimeInMillis();
            Date deadLine = new Date(validTime);
            boolean sendSmsFlag = userService.sendRegisterByMobileNumberSMS(mobileNumber, deadLine,remoteIp);
            if (!sendSmsFlag) {
                returnCode = ReturnMessage.SEND_SMS_IS_FAIL.getCode();
                log.info(mobileNumber + ":" + ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            }
        }

        baseResponseDto.setCode(returnCode);
        baseResponseDto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return baseResponseDto;
    }

    @Override
    public String verifyMobileNumber(String mobileNumber) {
        if (StringUtils.isEmpty(mobileNumber)) {
            log.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_NULL.getMsg());
            return ReturnMessage.MOBILE_NUMBER_IS_NULL.getCode();
        }
        try {
            vdtService.inputRuleValidation("input.mobile", mobileNumber);
        } catch (NoMatchingObjectsException | InputRuleMatchingException e) {
            log.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode();
        }
        User user = userBO.getUserByMobileNumber(mobileNumber);
        if (user != null) {
            log.info(mobileNumber + ":" + ReturnMessage.MOBILE_NUMBER_IS_EXIST.getMsg());
            return ReturnMessage.MOBILE_NUMBER_IS_EXIST.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public String verifyUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return ReturnMessage.USER_NAME_IS_NULL.getCode();
        }
        try {
            vdtService.inputRuleValidation("input.username", userName);
        } catch (NoMatchingObjectsException | InputRuleMatchingException e) {
            log.info(userName + ":" + ReturnMessage.USER_NAME_IS_INVALID.getMsg());
            return ReturnMessage.USER_NAME_IS_INVALID.getCode();
        }
        User user = userBO.getUserByUsername(userName);
        if (user != null) {
            log.info(userName + ":" + ReturnMessage.USER_NAME_IS_EXIST.getMsg());
            return ReturnMessage.USER_NAME_IS_EXIST.getCode();
        }

        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public String verifyReferrer(String referrer) {
        if (StringUtils.isEmpty(referrer)) {
            return ReturnMessage.SUCCESS.getCode();
        }
        User user = userBO.getUserByUsername(referrer);
        if (user != null) {
            log.info(referrer + ":" + ReturnMessage.REFERRER_IS_EXIST.getCode());
            return ReturnMessage.REFERRER_IS_EXIST.getCode();
        }

        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public String verifyCaptcha(String mobileNumber, String captcha) {
        try {
            authService.verifyAuthInfo(null, mobileNumber, captcha,
                    CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
        } catch (NoMatchingObjectsException e) {
            log.info(mobileNumber + "&&" + captcha + ":" + ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
            return ReturnMessage.SMS_CAPTCHA_ERROR.getCode();
        } catch (AuthInfoOutOfDateException e) {
            log.info(mobileNumber + "&&" + captcha + ":" + ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getMsg());
            return ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getCode();
        } catch (AuthInfoAlreadyActivedException e) {
            log.info(mobileNumber + "&&" + captcha + ":" + ReturnMessage.USER_IS_ACTIVE.getMsg());
            return ReturnMessage.USER_IS_ACTIVE.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
    }


    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        RegisterResponseDto registerResponseDto = new RegisterResponseDto();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        returnCode = this.verifyUserName(registerRequestDto.getUserName());
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            returnCode = this.verifyPassword(registerRequestDto.getPassword());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            returnCode = this.verifyMobileNumber(registerRequestDto.getPhoneNum());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            returnCode = this.verifyReferrer(registerRequestDto.getReferrer());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            returnCode = this.verifyCaptcha(registerRequestDto.getPhoneNum(), registerRequestDto.getCaptcha());
        }
        User user = registerRequestDto.convertToUser();
        try {
            userService.registerByMobileNumber(user, registerRequestDto.getCaptcha(), registerRequestDto.getReferrer());
        } catch (NoMatchingObjectsException e) {
            log.info(registerRequestDto.getPhoneNum() + "&&" + registerRequestDto.getCaptcha() + ":" + ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            returnCode = ReturnMessage.SMS_CAPTCHA_ERROR.getCode();
        } catch (AuthInfoOutOfDateException e) {
            log.info(registerRequestDto.getPhoneNum() + "&&" + registerRequestDto.getCaptcha() + ":" + ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getCode());
            returnCode = ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getCode();
        } catch (AuthInfoAlreadyActivedException e) {
            log.info(registerRequestDto.getPhoneNum() + "&&" + registerRequestDto.getCaptcha() + ":" + ReturnMessage.USER_IS_ACTIVE.getCode());
            returnCode = ReturnMessage.USER_IS_ACTIVE.getCode();
        }

        registerResponseDto.setCode(returnCode);
        registerResponseDto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            RegisterDataDto registerDataDto = new RegisterDataDto();
            registerDataDto.setUserId(registerRequestDto.getUserName());
            registerDataDto.setUserName(registerRequestDto.getUserName());
            registerDataDto.setToken(registerRequestDto.getBaseParam().getToken());
        }
        return registerResponseDto;
    }

    @Override
    public String verifyPassword(String password) {
        try {
            vdtService.inputRuleValidation("input.password", password);
        } catch (NoMatchingObjectsException e) {
            log.info(password + ":" + ReturnMessage.PASSWORD_IS_INVALID.getMsg());
            return ReturnMessage.PASSWORD_IS_INVALID.getCode();
        } catch (InputRuleMatchingException e) {
            log.info(password + ":" + ReturnMessage.PASSWORD_IS_INVALID.getMsg());
            return ReturnMessage.PASSWORD_IS_INVALID.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
    }


}
