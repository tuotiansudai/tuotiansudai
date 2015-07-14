package com.ttsd.mobile.service;

import javax.servlet.http.HttpSession;

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
     * @param operationType 操作类型
     * @return String “ture”或“false”
     */
    boolean mobileRegister(String userName,String password,String phoneNum,String vCode,String operationType);

    /**
     * @function 校验用户注册的用户名是否已存在
     * @param userName 用户名
     * @return String “ture”或“false”
     */
    boolean validateUserName(String userName);

    /**
     * @function 校验用注册的手机号已存在
     * @param phoneNum 手机号
     * @return String “ture”或“false”
     */
    boolean validateMobilePhoneNum(String phoneNum);

    /**
     * @function 校验用户输入的注册授权码是否正确
     * @param phoneNum 手机号
     * @param vCode 授权码
     * @return String “ture”或“false”
     */
    boolean validateVCode(String phoneNum,String vCode);
}
