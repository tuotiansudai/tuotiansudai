package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by hourglasskoala on 15/6/19.
 */
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    public static String SHA = "SHA";

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
        return userMapper.findUserByLoginName(referrer) != null;
    }

    private String getRandomSalt(){
        return UUID.randomUUID().toString().replace("-","");
    }

    private String encodeSHA(String data) throws Exception{
        MessageDigest md = MessageDigest.getInstance(SHA);
        byte[] digest = md.digest(data.getBytes());
        return new HexBinaryAdapter().marshal(digest);
    }
}
