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
import com.ttsd.api.service.MobileAppChannelService;
import com.ttsd.api.service.MobileAppRegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MobileAppRegisterServiceImpl implements MobileAppRegisterService {

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

    @Autowired
    private MobileAppChannelService mobileAppChannelService;


    @Override
    public BaseResponseDto sendRegisterByMobileNumberSMS(String mobileNumber,String remoteIp) {
        BaseResponseDto dto = new BaseResponseDto();
        String returnCode = this.verifyMobileNumber(mobileNumber);
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            
            boolean sendSmsFlag = userService.sendSmsMobileNumber(mobileNumber, remoteIp, CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
            if (!sendSmsFlag) {
                returnCode = ReturnMessage.SEND_SMS_IS_FAIL.getCode();
                log.info(mobileNumber + ":" + ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            }
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
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
            log.error(e.getLocalizedMessage(), e);
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
            String regex = "(?!^\\d+$)^\\w{5,25}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(userName);
            if (!matcher.matches()) {
                // 验证失败
                throw new InputRuleMatchingException("用户名格式不正确");
            }
        } catch (InputRuleMatchingException e) {
            log.error(e.getLocalizedMessage(), e);
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
        if(StringUtils.isEmpty(referrer)){
            return ReturnMessage.SUCCESS.getCode();
        }
        User user = userBO.getUserByUsername(referrer);
        if (user == null) {
            log.info(referrer + ":" + ReturnMessage.REFERRER_IS_NOT_EXIST.getCode());
            return ReturnMessage.REFERRER_IS_NOT_EXIST.getCode();
        }

        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public String verifyCaptcha(String mobileNumber, String captcha) {
        try {
            authService.verifyAuthInfo(null, mobileNumber, captcha,
                    CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
        } catch (NoMatchingObjectsException  e) {
            log.error(e.getLocalizedMessage(), e);
            return ReturnMessage.SMS_CAPTCHA_ERROR.getCode();
        } catch (AuthInfoOutOfDateException e) {
            log.error(e.getLocalizedMessage(), e);
            return ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getCode();
        } catch (AuthInfoAlreadyActivedException e) {
            log.error(e.getLocalizedMessage(), e);
            return ReturnMessage.USER_IS_ACTIVE.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
    }


    @Override
    public BaseResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String returnCode = this.verifyUserName(registerRequestDto.getUserName());
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

        try {
            if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
                User user = registerRequestDto.convertToUser();
                user.setSource(AccessSource.valueOf(registerRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)).name());
                String channel = mobileAppChannelService.obtainChannelBySource(registerRequestDto.getBaseParam());
                user.setChannel(channel);
                userService.registerByMobileNumber(user, registerRequestDto.getCaptcha(), registerRequestDto.getReferrer());
            }

        } catch (NoMatchingObjectsException e) {
            log.error(e.getLocalizedMessage(), e);
            returnCode = ReturnMessage.SMS_CAPTCHA_ERROR.getCode();
        } catch (AuthInfoOutOfDateException e) {
            log.error(e.getLocalizedMessage(), e);
            returnCode = ReturnMessage.SMS_CAPTCHA_IS_OVERDUE.getCode();
        } catch (AuthInfoAlreadyActivedException e) {
            log.error(e.getLocalizedMessage(), e);
            returnCode = ReturnMessage.USER_IS_ACTIVE.getCode();
        }catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            RegisterResponseDataDto registerDataDto = new RegisterResponseDataDto();
            registerDataDto.setUserId(registerRequestDto.getUserName());
            registerDataDto.setUserName(registerRequestDto.getUserName());
            registerDataDto.setPhoneNum(registerRequestDto.getPhoneNum());
            dto.setData(registerDataDto);
        }
        return dto;
    }



    @Override
    public String verifyPassword(String password) {
        try {
            String regex = "^(?=.*[^\\d])(.{6,20})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                // 验证失败
                throw new InputRuleMatchingException("密码格式不正确");
            }
        } catch (InputRuleMatchingException e) {
            log.error(e.getLocalizedMessage(), e);
            return ReturnMessage.PASSWORD_IS_INVALID.getCode();
        }
        return ReturnMessage.SUCCESS.getCode();
    }

}
