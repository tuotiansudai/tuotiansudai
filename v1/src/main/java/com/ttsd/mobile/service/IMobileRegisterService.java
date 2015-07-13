package com.ttsd.mobile.service;

import javax.servlet.http.HttpSession;

/**
 * Created by tuotian on 15/7/9.
 */
public interface IMobileRegisterService {
    public String mobileRegister(String userName,String password,String phoneNum,String vCode,String operationType);
}
