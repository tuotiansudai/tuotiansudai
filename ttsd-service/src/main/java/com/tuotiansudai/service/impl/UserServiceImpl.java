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
        return userMapper.findUserByEmail(email) != null;
    }

    @Override
    public boolean isExistMobileNumber(String mobileNumber) throws Exception {
        return userMapper.findUserByMobileNumber(mobileNumber) != null;
    }

    @Override
    public boolean isExistReferrer(String referrer) throws Exception {
        return userMapper.findReferrerByLoginName(referrer) != null;
    }
}
