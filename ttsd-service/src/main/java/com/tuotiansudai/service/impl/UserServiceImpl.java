package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    public enum userStatus{
        inactive,active;
    }

    @Override
    public boolean userEmailIsExisted(String email) {
        return userMapper.findUserByEmail(email) != null;
    }

    @Override
    public boolean userMobileNumberIsExisted(String mobileNumber) {
        return userMapper.findUserByMobileNumber(mobileNumber) != null;
    }

    @Override
    public boolean referrerIsExisted(String referrer) {
        return userMapper.findUserByLoginName(referrer) != null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void registerUser(UserModel userModel) throws Exception{
        String randomSalt = getRandomSalt();
        userModel.setSalt(randomSalt);
        userModel.setRegisterTime(new Date());
        userModel.setStatus(userStatus.active);
        this.userMapper.insertUser(userModel);
    }

    private String getRandomSalt(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
