package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public static String SHA = "SHA";

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
    public boolean loginNameIsExisted(String loginName) {
        return userMapper.findUserByLoginName(loginName) != null;
    }
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean registerUser(UserModel userModel) {
        String randomSalt = getRandomSalt();
        String password;
        try {
            password = encodeSHA(encodeSHA(userModel.getPassword()) + randomSalt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        userModel.setSalt(randomSalt);
        userModel.setRegisterTime(new Date());
        userModel.setPassword(password);
        userModel.setStatus(UserStatus.ACTIVE);
        this.userMapper.insertUser(userModel);
        return true;
    }

    private String getRandomSalt(){
        return UUID.randomUUID().toString().replace("-","");
    }

    private String encodeSHA(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA);
        byte[] digest = md.digest(data.getBytes());
        return new HexBinaryAdapter().marshal(digest);
    }
}
