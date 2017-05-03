package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

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

    @Autowired
    private UserOpLogService userOpLogService;

    @Autowired
    private LoginNameGenerator loginNameGenerator;

    @Autowired
    private MQWrapperClient mqWrapperClient;

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
    @Transactional
    public boolean registerUser(RegisterUserDto dto) {
        if (this.mobileIsExist(dto.getMobile())) {
            logger.warn(MessageFormat.format("[Register User {0}] mobile is existed", dto.getMobile()));
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

        UserModel userModel = new UserModel();
        userModel.setLoginName(loginNameGenerator.generate());
        userModel.setMobile(dto.getMobile());
        userModel.setSource(dto.getSource());
        userModel.setReferrer(referrerUserModel != null ? referrerUserModel.getLoginName() : null);
        userModel.setChannel(dto.getChannel());
        String salt = myShaPasswordEncoder.generateSalt();
        userModel.setSalt(salt);
        userModel.setPassword(myShaPasswordEncoder.encodePassword(dto.getPassword(), salt));
        userModel.setLastModifiedTime(new Date());
        boolean register = registerUserService.register(userModel);

        mqWrapperClient.sendMessage(MessageQueue.WeChatBoundNotify, new WeChatBoundMessage(dto.getMobile(), dto.getOpenid()));//登录成功绑定微信号

        return register;
    }

    @Transactional
    public boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId) {

        boolean returnValue = false;

        boolean correct = this.verifyPasswordCorrect(loginName, originalPassword);

        if (correct) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            String mobile = userModel.getMobile();

            userModel.setPassword(myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt()));
            userMapper.updateUser(userModel);
            smsWrapperClient.sendPasswordChangedNotify(mobile);

            returnValue = true;
        }

        // 发送用户行为日志 MQ
        userOpLogService.sendUserOpLogMQ(loginName, ip, platform, deviceId, UserOpType.CHANGE_PASSWORD, returnValue ? "Success" : "Fail");
        return returnValue;
    }

    @Override
    public boolean verifyPasswordCorrect(String loginName, String password) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel.getPassword().equals(myShaPasswordEncoder.encodePassword(password, userModel.getSalt()));
    }

    @Override
    public boolean mobileIsRegister(String mobile) {
        return mobileIsExist(mobile) || prepareUserMapper.findByMobile(mobile) != null;
    }

    @Override
    public UserModel findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    @Override
    public long getExperienceBalanceByLoginName(String loginName) {
        Long experienceBalance = userMapper.findExperienceByLoginName(loginName);
        return experienceBalance != null ? experienceBalance : 0;
    }
}
