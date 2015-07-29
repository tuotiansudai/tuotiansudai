package com.ttsd.api.service;

/**
 * Created by tuotian on 15/7/29.
 */
public interface RetrievePasswordService {
    /**
     * @function 修改密码
     * @param phoneNum 手机号
     * @param password 密码
     * @return String
     */
    String retrievePassword(String phoneNum,String password);

    /**
     * @function 校验验证码
     * @param phoneNum 手机号
     * @param authCode 验证码
     * @return String
     */
    String validateAuthCode(String phoneNum,String authCode);
}
