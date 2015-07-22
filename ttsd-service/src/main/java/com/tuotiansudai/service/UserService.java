package com.tuotiansudai.service;

import com.tuotiansudai.dto.RegisterDto;

public interface UserService {

    boolean userEmailIsExisted(String email);

    boolean userMobileNumberIsExisted(String mobileNumber);

    boolean registerUser(RegisterDto userModel);

    boolean referrerIsExisted(String referrer);

    boolean loginNameIsExisted(String loginName);
}
