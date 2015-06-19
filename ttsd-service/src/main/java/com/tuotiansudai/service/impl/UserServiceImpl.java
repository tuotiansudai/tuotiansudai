package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hourglasskoala on 15/6/19.
 */
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public boolean isExistEmail(String email) throws Exception {
        boolean isExistEmailFlag = false;
        UserModel userModel = userMapper.findUserByEmail(email);
        if (userModel != null){
            isExistEmailFlag = true;
        }
        return isExistEmailFlag;
    }

    @Override
    public boolean isExistMobileNumber(String mobileNumber) throws Exception {
        boolean isExistMobileNumber = false;
        UserModel userModel = userMapper.findUserByMobileNumber(mobileNumber);
        if (userModel != null){
            isExistMobileNumber = true;
        }
        return isExistMobileNumber;
    }

    @Override
    public boolean isExistReferrer(String referrer) throws Exception {
        boolean isExistReferrer = false;
        UserModel userModel = userMapper.findReferrerByLoginName(referrer);
        if (userModel != null){
            isExistReferrer = true;
        }
        return isExistReferrer;
    }
}
