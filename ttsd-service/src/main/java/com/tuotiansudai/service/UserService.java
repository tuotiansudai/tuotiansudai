package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.UserModel;

/**
 * Created by hourglasskoala on 15/6/18.
 */
public interface UserService {

    public boolean userEmailIsExisted(String email);

    public boolean userMobileNumberIsExisted(String mobileNumber);

    public void registerUser(UserModel userModel) throws Exception;

    public boolean referrerIsExisted(String referrer);
}
