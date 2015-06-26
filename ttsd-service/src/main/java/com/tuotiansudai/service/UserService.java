package com.tuotiansudai.service;

public interface UserService {

    boolean userEmailIsExisted(String email);

    boolean userMobileNumberIsExisted(String mobileNumber);

    boolean referrerIsExisted(String referrer);
}
