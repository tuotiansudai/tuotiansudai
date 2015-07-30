package com.ttsd.api.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.google.common.base.Strings;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.RetrievePasswordRequestDto;
import com.ttsd.api.dto.RetrievePasswordResponseDto;
import com.ttsd.api.dto.SendSmsRequestDto;
import com.ttsd.api.service.RetrievePasswordService;
import com.ttsd.mobile.dao.IMobileRegisterDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuotian on 15/7/29.
 */
@Service(value = "RetrievePasswordServiceImpl")
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

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
    public RetrievePasswordResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) throws UserNotFoundException {
        RetrievePasswordResponseDto retrievePasswordResponseDto = new RetrievePasswordResponseDto();
        String phoneNumber = retrievePasswordRequestDto.getPhoneNum();
        String authCode = retrievePasswordRequestDto.getValidateCode();
        String password = retrievePasswordRequestDto.getPassword();
        if (Strings.isNullOrEmpty(phoneNumber)){
            //手机号不能为空
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
            return retrievePasswordResponseDto;
        }
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
            return retrievePasswordResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber, regStr)){
            //你输入的手机号不符合规则
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
            return retrievePasswordResponseDto;
        }
        if (Strings.isNullOrEmpty(password)){
            //密码不能为空
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
            return retrievePasswordResponseDto;
        }

        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE);
        if (codeCount == 1){
            User user = userService.getUserByMobileNumber(phoneNumber);
            userService.modifyPassword(user.getId(), password);
            //修改成功
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
        }else {
            //校验码错误
            retrievePasswordResponseDto.setCode("");
            retrievePasswordResponseDto.setMessage("");
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
        if (Strings.isNullOrEmpty(phoneNumber)){
            //手机号不能为空
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            return baseResponseDto;
        }
        if (Strings.isNullOrEmpty(authCode)){
            //验证码不能为空
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            return baseResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //你输入的手机号不符合规则
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            return baseResponseDto;
        }
        int codeCount = mobileRegisterDao.getAuthInfo(phoneNumber, authCode, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE);
        if (codeCount == 1){
            //验证码输入正确
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
        }else {
            //验证码输入错误
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
        }
        return baseResponseDto;
    }


    /**
     * @funtion 发送手机验证码
     * @param sendSmsRequestDto 发送手机验证码参数封装类
     * @return String
     */
    @Override
    public BaseResponseDto sendSMS(SendSmsRequestDto sendSmsRequestDto,String remoteIp) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String authType = sendSmsRequestDto.getType();
        String phoneNumber = sendSmsRequestDto.getPhoneNum();
        if (Strings.isNullOrEmpty(authType)){
            //验证码类型不能为空
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            return baseResponseDto;
        }
        String regStr = "^1\\d{10}$";
        if (!regValidateTarget(phoneNumber,regStr)){
            //手机号格式不符合要求
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            return baseResponseDto;
        }
        if (userService.sendRegisterByMobileNumberSMS(phoneNumber,remoteIp,authType)){
            //手机验证码发送成功
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
        }else {
            //手机验证码发送失败
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
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


