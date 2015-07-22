package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.RegisterDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.SmsCaptchaService;
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

    @Autowired
    private SmsCaptchaService smsCaptchaService;

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
    public boolean registerUser(RegisterDto registerDto) {
        boolean loginNameIsExisted = this.loginNameIsExisted(registerDto.getLoginName());
        boolean mobileNumberIsExisted = this.userMobileNumberIsExisted(registerDto.getMobileNumber());
        boolean userEmailIsExisted = this.userEmailIsExisted(registerDto.getEmail());
        boolean referrerIsNotExisted = !Strings.isNullOrEmpty(registerDto.getReferrer()) && !this.referrerIsExisted(registerDto.getReferrer());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyCaptcha(registerDto.getMobileNumber(), registerDto.getCaptcha());

        if (loginNameIsExisted || mobileNumberIsExisted || userEmailIsExisted || referrerIsNotExisted || verifyCaptchaFailed) {
            return false;
        }

        UserModel registerUserModel = registerDto.convertToUserModel();

        if (!Strings.isNullOrEmpty(registerDto.getReferrer())) {
            UserModel referrer = userMapper.findUserByLoginName(registerDto.getReferrer());
            registerUserModel.setReferrerId(referrer.getId());
        }

        String randomSalt = getRandomSalt();
        String password;
        try {
            password = encodeSHA(encodeSHA(registerUserModel.getPassword()) + randomSalt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        registerUserModel.setSalt(randomSalt);
        registerUserModel.setRegisterTime(new Date());
        registerUserModel.setPassword(password);
        registerUserModel.setStatus(UserStatus.ACTIVE);
        this.userMapper.insertUser(registerUserModel);
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
