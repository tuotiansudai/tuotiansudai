package com.ttsd.api.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.google.common.base.Strings;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppRetrievePasswordService;
import com.ttsd.mobile.dao.IMobileRegisterDao;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuotian on 15/7/29.
 */
@Service(value = "mobileAppRetrievePasswordServiceImpl")
public class MobileAppRetrievePasswordServiceImpl implements MobileAppRetrievePasswordService {

    @Logger
    private Log log;

    @Resource(name = "mobileRegisterDaoImpl")
    private IMobileRegisterDao mobileRegisterDao;

    @Resource
    private UserService userService;

    /**
     * @function 修改密码
     * @param retrievePasswordRequestDto 修改密码参数封装类
     * @return String
     */
    @Override
    @Transactional
    public BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) throws UserNotFoundException {
        BaseResponseDto dto = new BaseResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        String password = retrievePasswordRequestDto.getPassword();
        String authType = CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE;
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            dto.setCode(ReturnMessage.SMS_CAPTCHA_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_IS_NULL.getMsg());
            return dto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber, regStr)){
            //你输入的手机号不符合规则
            dto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            dto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return dto;
        }
        String passwordRegStr = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (!regValidateTarget(password,passwordRegStr)){
            //密码过于简单
            dto.setCode(ReturnMessage.PASSWORD_IS_INVALID.getCode());
            dto.setMessage(ReturnMessage.PASSWORD_IS_INVALID.getMsg());
            return dto;
        }
        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, authType);
        if (codeCount == 1){
            User user = userService.getUserByMobileNumber(phoneNumber);
            userService.modifyPassword(user.getId(), password);
            //修改成功
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }else {
            //校验码错误
            dto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
        return dto;
    }


    /**
     * @function 校验手机验证码
     * @param retrievePasswordRequestDto 校验验证码参数封装类
     * @return BaseResponseDto
     */
    @Override
    public BaseResponseDto validateAuthCode(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        String authType = CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE;
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            dto.setCode(ReturnMessage.SMS_CAPTCHA_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_IS_NULL.getMsg());
            return dto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //你输入的手机号不符合规则
            dto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            dto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return dto;
        }
        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, authType);
        if (codeCount == 1){
            //验证码输入正确
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }else {
            //验证码输入错误
            dto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
        return dto;
    }


    /**
     * @funtion 发送手机验证码
     * @param retrievePasswordRequestDto 发送手机验证码参数封装类
     * @return String
     */
    @Override
    public BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto,String remoteIp) {
        BaseResponseDto dto = new BaseResponseDto();
        String authType = retrievePasswordRequestDto.getAuthType();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        if (Strings.isNullOrEmpty(authType)){
            //验证码类型不能为空
            dto.setCode(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getMsg());
            return dto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //手机号格式不符合要求
            dto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            dto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return dto;
        }
        int count = mobileRegisterDao.getUserCountByCellphone(phoneNumber);
        if (count < 1){
            dto.setCode(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getCode());
            dto.setMessage(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getMsg());
            return dto;
        }
        try {
            if (userService.sendSmsMobileNumber(phoneNumber, remoteIp, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE)){
                //手机验证码发送成功
                dto.setCode(ReturnMessage.SUCCESS.getCode());
                dto.setMessage(ReturnMessage.SUCCESS.getMsg());

            }else {
                log.error("send retrieve password message fail , cause by message interface fault !");
                dto.setCode(ReturnMessage.SEND_SMS_IS_FAIL.getCode());
                dto.setMessage(ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            }
            return dto;
        }catch (Exception e){
            //手机验证码发送失败
            log.error("send retrieve password message fail , cause by server fault !");
            log.error(e.getLocalizedMessage(),e);
            dto.setCode(ReturnMessage.SEND_SMS_IS_FAIL.getCode());
            dto.setMessage(ReturnMessage.SEND_SMS_IS_FAIL.getMsg());
            return dto;
        }
    }


    /**
     * @function 正则校验
     * @param target 需要校验的字符串
     * @param regStr 正则表达式
     * @return boolean 通过校验返回true，否则返回false
     */
    public boolean regValidateTarget(String target,String regStr){
        if(Strings.isNullOrEmpty(target)){
            return false;
        }
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(target);
        matcher.matches();
        return matcher.matches();
    }

}


