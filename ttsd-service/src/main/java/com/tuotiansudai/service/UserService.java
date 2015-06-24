package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.UserModel;

import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hourglasskoala on 15/6/18.
 */
public interface UserService {

    public boolean userEmailIsExisted(String email) throws Exception;

    public boolean userMobileNumberIsExisted(String mobileNumber) throws Exception;

    public boolean isExistReferrer(String referrer) throws  Exception;

    public void registerUser(UserModel userModel) throws Exception;
    public boolean referrerIsExisted(String referrer) throws Exception;
}
