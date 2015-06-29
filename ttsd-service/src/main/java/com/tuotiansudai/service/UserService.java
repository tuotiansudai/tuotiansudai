package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.UserModel;

public interface UserService {

    boolean userEmailIsExisted(String email);

    boolean userMobileNumberIsExisted(String mobileNumber);

    boolean registerUser(UserModel userModel);

    boolean referrerIsExisted(String referrer);
}
