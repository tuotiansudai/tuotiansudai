package com.tuotiansudai.service;


public interface ImpersonateService {

    boolean impersonateLogin(String securityCode);

    String plantSecurityCode(String loginName);
}
