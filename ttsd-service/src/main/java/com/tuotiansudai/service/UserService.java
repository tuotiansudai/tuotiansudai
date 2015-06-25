package com.tuotiansudai.service;

public interface UserService {

    public boolean userEmailIsExisted(String email);

    public boolean userMobileNumberIsExisted(String mobileNumber);

    public boolean referrerIsExisted(String referrer);
}
