package com.tuotiansudai.service;


public interface ImpersonateService {

    String impersonateLogin(String securityCode);

    String plantSecurityCode(String adminLoginName, String loginName);
}
