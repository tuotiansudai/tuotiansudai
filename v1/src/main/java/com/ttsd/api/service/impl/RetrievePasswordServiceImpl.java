package com.ttsd.api.service.impl;

import com.ttsd.api.service.RetrievePasswordService;
import org.springframework.stereotype.Service;

/**
 * Created by tuotian on 15/7/29.
 */
@Service(value = "RetrievePasswordServiceImpl")
public class RetrievePasswordServiceImpl implements RetrievePasswordService {
    /**
     * @function 修改密码
     * @param phoneNum 手机号
     * @param password 密码
     * @return String
     */
    @Override
    public String  retrievePassword(String phoneNum, String password) {

        return null;
    }

    /**
     * @function 校验手机验证码
     * @param phoneNum 手机号
     * @param authCode 验证码
     * @return
     */
    @Override
    public String validateAuthCode(String phoneNum, String authCode) {
        return null;
    }
}
