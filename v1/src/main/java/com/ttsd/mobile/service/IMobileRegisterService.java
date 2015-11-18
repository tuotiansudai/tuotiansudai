package com.ttsd.mobile.service;

import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;

/**
 * Created by tuotian on 15/7/9.
 */
public interface IMobileRegisterService {
    /**
     * @function 手机端用户注册
     * @param userName 用户名
     * @param password 密码
     * @param phoneNum 手机号
     * @param vCode 验证码
     * @param referrer 推荐人
     * @return boolean 注册成功，返回true，否则返回false
     */
    boolean mobileRegister(String userName,String password,String phoneNum,String vCode,String referrer) throws AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, NoMatchingObjectsException;

    /**
     * @function 获取生成的授权码
     * @param phoneNumber
     * @param remoteIp
     * @return boolean 授权码发送成功，返回true，否则返回false
     */

    boolean getCreatedValidateCode(String phoneNumber,String remoteIp);

    /**
     * @function 校验用户注册的用户名是否已存在
     * @param userName 用户名
     * @return boolean 通过校验，返回ture,否则返回false
     */
    boolean validateUserName(String userName);

    /**
     * @function 校验用户输入的密码
     * @param password 密码
     * @return boolean 通过校验，返回ture,否则返回false
     */
    boolean validatePassword(String password);

    /**
     * @function 校验用注册的手机号已存在
     * @param phoneNumber 手机号
     * @return boolean 通过校验，返回ture,否则返回false
     */
    boolean validateMobilePhoneNum(String phoneNumber);

    /**
     * @function 校验用户输入的注册授权码是否正确
     * @param phoneNumber 手机号
     * @param vCode 授权码
     * @return boolean 通过校验，返回ture,否则返回false
     */
    boolean validateVCode(String phoneNumber,String vCode);

    /**
     * @function 校验推荐人是否存在
     * @param referrer 推荐人
     * @return boolean 通过校验，返回ture,否则返回false
     */
    boolean validateReferrer(String referrer);
}
