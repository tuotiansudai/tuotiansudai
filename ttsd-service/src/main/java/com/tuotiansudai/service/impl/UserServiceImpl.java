package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public boolean userEmailIsExisted(String email) throws Exception {
        return userMapper.findUserByEmail(email) != null;
    }

    @Override
    public boolean userMobileNumberIsExisted(String mobileNumber) throws Exception {
        return userMapper.findUserByMobileNumber(mobileNumber) != null;
    }

    @Override
    public boolean referrerIsExisted(String referrer) throws Exception {
        return userMapper.findUserByLoginName(referrer) != null;
    }
}
