package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.MobileLocationUtils;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import com.tuotiansudai.util.RandomStringGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;


    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private RegisterUserService registerUserService;

    public static String SHA = "SHA";

    private final static int LOGIN_NAME_LENGTH = 8;

    @Override
    public String getMobile(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel != null ? userModel.getMobile() : null;
    }

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
    public boolean loginNameOrMobileIsExist(String loginNameOrMobile) {
        return userMapper.findByLoginNameOrMobile(loginNameOrMobile) != null;
    }

    @Override
    public boolean isIdentityNumberExist(String identityNumber) {
        return userMapper.findByIdentityNumber(identityNumber) != null;
    }

    @Override
    public String getRealName(String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        return userModel == null ? loginNameOrMobile : userModel.getUserName();
    }

    @Override
    public boolean registerUser(RegisterUserDto dto) {
        if (this.mobileIsExist(dto.getMobile())) {
            logger.error(MessageFormat.format("[Register User {0}] mobile is existed", dto.getMobile()));
            return false;
        }

        if (!this.smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA)) {
            logger.error(MessageFormat.format("[Register User {0}] captcha({1}) is not match", dto.getMobile(), dto.getCaptcha()));
            return false;
        }

        PrepareUserModel prepareUserModel = prepareUserMapper.findByMobile(dto.getMobile());
        String referrer = prepareUserModel != null ? prepareUserModel.getReferrerMobile() : dto.getReferrer();
        UserModel referrerUserModel = userMapper.findByLoginNameOrMobile(referrer);
        if (!Strings.isNullOrEmpty(referrer) && referrerUserModel == null) {
            logger.error(MessageFormat.format("[Register User {0}] referrer({1}) is not existed", dto.getMobile(), referrer));
            return false;
        }


        String loginName = dto.getLoginName();
        boolean autoGenerateLoginName = Strings.isNullOrEmpty(loginName);

        if (!autoGenerateLoginName && this.loginNameIsExist(loginName)) {
            logger.error(MessageFormat.format("[Register User {0}] login name ({1}) is existed", dto.getMobile(), loginName));
            return false;
        }

        if (autoGenerateLoginName) {
            int tryTimes = 0;
            do {
                tryTimes += 1;
                if (tryTimes > 20) {
                    logger.info(MessageFormat.format("[Register User {0}] auto generate login name reach max times", dto.getMobile()));
                    return false;
                }
                loginName = RandomStringGenerator.generate(LOGIN_NAME_LENGTH);
            } while (this.loginNameIsExist(loginName));
            dto.setLoginName(loginName);
        }


        try {
            UserModel userModel = new UserModel();
            userModel.setLoginName(loginName);
            userModel.setMobile(dto.getMobile());
            userModel.setSource(dto.getSource());
            userModel.setReferrer(referrerUserModel != null ? referrerUserModel.getLoginName() : null);
            userModel.setChannel(dto.getChannel());
            String salt = myShaPasswordEncoder.generateSalt();
            userModel.setSalt(salt);
            userModel.setPassword(myShaPasswordEncoder.encodePassword(dto.getPassword(), salt));
            userModel.setLastModifiedTime(new Date());
            return registerUserService.register(userModel);
        } catch (ReferrerRelationException e) {
            logger.error(MessageFormat.format("[Register User {0}] create new user is failed", dto.getMobile()));

        }

        return false;
    }

    @Override
    @Transactional
    public boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId) {

        boolean correct = this.verifyPasswordCorrect(loginName, originalPassword);

        if (!correct) {
            return false;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel.getMobile();

        userModel.setPassword(myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt()));
        userMapper.updateUser(userModel);
        smsWrapperClient.sendPasswordChangedNotify(mobile);
        return true;
    }

    @Override
    public boolean verifyPasswordCorrect(String loginName, String password) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel.getPassword().equals(myShaPasswordEncoder.encodePassword(password, userModel.getSalt()));
    }

    @Transactional
    @Override
    public void refreshAreaByMobile(List<UserModel> userModels) {
        for (UserModel userModel : userModels) {
            String phoneMobile = userModel.getMobile();
            if (StringUtils.isNotEmpty(phoneMobile)) {
                String[] provinceAndCity = MobileLocationUtils.locateMobileNumber(phoneMobile);
                if (StringUtils.isEmpty(provinceAndCity[0])) {
                    provinceAndCity[0] = "未知";
                }
                if (StringUtils.isEmpty(provinceAndCity[1])) {
                    provinceAndCity[1] = "未知";
                }
                userModel.setProvince(provinceAndCity[0]);
                userModel.setCity(provinceAndCity[1]);
            } else {
                userModel.setProvince("未知");
                userModel.setCity("未知");
            }
            userModel.setLastModifiedTime(new Date());
            userMapper.updateUser(userModel);
        }
    }

    @Override
    public void refreshAreaByMobileInJob() {
        while (true) {
            List<UserModel> userModels = userMapper.findUsersByProvince();
            if (CollectionUtils.isEmpty(userModels)) {
                break;
            }
            ((UserService) AopContext.currentProxy()).refreshAreaByMobile(userModels);
        }
    }

    @Override
    public boolean mobileIsRegister(String mobile) {
        return mobileIsExist(mobile) || prepareUserMapper.findByMobile(mobile) != null;
    }

    @Override
    public UserModel findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }
}
