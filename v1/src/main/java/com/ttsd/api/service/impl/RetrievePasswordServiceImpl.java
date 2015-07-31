package com.ttsd.api.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.google.common.base.Strings;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.RetrievePasswordService;
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
@Service(value = "RetrievePasswordServiceImpl")
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

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
    public RetrievePasswordResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) throws UserNotFoundException {
        RetrievePasswordResponseDto retrievePasswordResponseDto = new RetrievePasswordResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        String password = retrievePasswordRequestDto.getPassword();
        String authType = CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE;
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            retrievePasswordResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_IS_NULL.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_IS_NULL.getMsg());
            return retrievePasswordResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber, regStr)){
            //你输入的手机号不符合规则
            retrievePasswordResponseDto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return retrievePasswordResponseDto;
        }
        String passwordRegStr = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (!regValidateTarget(password,passwordRegStr)){
            //密码过于简单
            retrievePasswordResponseDto.setCode(ReturnMessage.PASSWORD_IS_INVALID.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.PASSWORD_IS_INVALID.getMsg());
            return retrievePasswordResponseDto;
        }
        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, authType);
        if (codeCount == 1){
            User user = userService.getUserByMobileNumber(phoneNumber);
            userService.modifyPassword(user.getId(), password);
            //修改成功
            retrievePasswordResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }else {
            //校验码错误
            retrievePasswordResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
        return retrievePasswordResponseDto;
    }


    /**
     * @function 校验手机验证码
     * @param retrievePasswordRequestDto 校验验证码参数封装类
     * @return BaseResponseDto
     */
    @Override
    public BaseResponseDto validateAuthCode(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        String authType = CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE;
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_IS_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_IS_NULL.getMsg());
            return baseResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //你输入的手机号不符合规则
            baseResponseDto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            baseResponseDto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return baseResponseDto;
        }
        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, authType);
        if (codeCount == 1){
            //验证码输入正确
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }else {
            //验证码输入错误
            baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
            baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
        return baseResponseDto;
    }


    /**
     * @funtion 发送手机验证码
     * @param retrievePasswordRequestDto 发送手机验证码参数封装类
     * @return String
     */
    @Override
    public BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto,String remoteIp) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String authType = retrievePasswordRequestDto.getAuthType();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        if (Strings.isNullOrEmpty(authType)){
            //验证码类型不能为空
            baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_TYPE_IS_NULL.getMsg());
            return baseResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //手机号格式不符合要求
            baseResponseDto.setCode(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getCode());
            baseResponseDto.setMessage(ReturnMessage.MOBILE_NUMBER_IS_INVALID.getMsg());
            return baseResponseDto;
        }
        int count = mobileRegisterDao.getUserCountByCellphone(phoneNumber);
        if (count < 1){
            baseResponseDto.setCode(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getCode());
            baseResponseDto.setMessage(ReturnMessage.MOBILE_NUMBER_NOT_EXIST.getMsg());
            return baseResponseDto;
        }
        try {
            if (userService.sendRegisterByMobileNumberSMS(phoneNumber,remoteIp,authType)){
                //手机验证码发送成功
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }
        }catch (Exception e){
            //手机验证码发送失败
            log.error("send retrieve password message fail , cause by server fault !");
            log.error(e.getLocalizedMessage(),e);
            baseResponseDto.setCode(ReturnMessage.SEND_SMS_IS_FAIL.getCode());
            baseResponseDto.setMessage(ReturnMessage.SEND_SMS_IS_FAIL.getMsg());

        }
        return baseResponseDto;
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

    public void setMobileRegisterDao(IMobileRegisterDao mobileRegisterDao) {
        this.mobileRegisterDao = mobileRegisterDao;
    }
}


