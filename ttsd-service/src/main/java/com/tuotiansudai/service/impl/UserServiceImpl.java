package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private PayWrapperClient payWrapperClient;

    public static String SHA = "SHA";

    @Override
    public boolean emailIsExist(String email) {
        return userMapper.findByEmail(email) != null;
    }

    @Override
    public boolean mobileIsExist(String mobile) {
        return userMapper.findByMobile(mobile) != null;
    }

    @Override
    public boolean loginNameIsExist(String loginName) {
        return userMapper.findByLoginName(loginName) != null;
    }

    @Override
    @Transactional
    public boolean registerUser(RegisterUserDto dto) {
        boolean loginNameIsExist = this.loginNameIsExist(dto.getLoginName());
        boolean mobileIsExist = this.mobileIsExist(dto.getMobile());
        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !this.loginNameIsExist(dto.getReferrer());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyRegisterCaptcha(dto.getMobile(), dto.getCaptcha());

        if (loginNameIsExist || mobileIsExist || referrerIsNotExist || verifyCaptchaFailed) {
            return false;
        }

        UserModel userModel = dto.convertToUserModel();

        // TODO implement it by Spring Security
        String randomSalt = getRandomSalt();
        String password;
        try {
            password = encodeSHA(encodeSHA(userModel.getPassword()) + randomSalt);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
        userModel.setSalt(randomSalt);
        userModel.setPassword(password);
        this.userMapper.create(userModel);
        return true;
    }

    @Override
    public boolean registerAccount(RegisterAccountDto dto) {
        BaseDto baseDto = payWrapperClient.register(dto);
        return baseDto.getData().getStatus();
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
