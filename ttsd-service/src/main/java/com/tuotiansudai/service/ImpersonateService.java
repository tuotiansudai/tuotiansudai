package com.tuotiansudai.service;


public interface ImpersonateService {

    boolean impersonateLogin(String loginName, String randomCode);

    String plantRandomCode(String loginName);
}
