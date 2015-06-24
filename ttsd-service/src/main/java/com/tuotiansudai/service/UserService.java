package com.tuotiansudai.service;

public interface UserService {

    public boolean userEmailIsExisted(String email) throws Exception;

    public boolean userMobileNumberIsExisted(String mobileNumber) throws Exception;

    public boolean referrerIsExisted(String referrer) throws Exception;
}
