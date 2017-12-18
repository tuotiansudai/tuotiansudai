package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.request.ChangePasswordRequestDto;
import com.tuotiansudai.dto.request.RegisterRequestDto;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.ExperienceAccountMapper;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.service.RegisterUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperienceAccountMapper experienceAccountMapper;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private UserOpLogService userOpLogService;

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
        if (userModel == null || Strings.isNullOrEmpty(userModel.getUserName())) {
            return loginNameOrMobile;
        }
        return userModel.getUserName();
    }

    @Override
    @Transactional
    public boolean registerUser(RegisterUserDto dto) {
        if (this.mobileIsExist(dto.getMobile())) {
            logger.warn(MessageFormat.format("[Register User {0}] mobile is existed", dto.getMobile()));
            return false;
        }

        if (!this.smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), SmsCaptchaType.REGISTER_CAPTCHA)) {
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

        boolean register = registerUserCommon(dto, referrerUserModel);

        mqWrapperClient.sendMessage(MessageQueue.WeChatBoundNotify, new WeChatBoundMessage(dto.getMobile(), dto.getOpenid()));//登录成功绑定微信号

        return register;
    }

    @Override
    @Transactional
    public boolean registerUserFromHuizu(RegisterUserDto dto) {
        return registerUserCommon(dto, null);
    }

    private boolean registerUserCommon(RegisterUserDto dto, UserModel referrerUserModel) {
        RegisterRequestDto registerDto = new RegisterRequestDto(
                dto.getMobile(),
                dto.getPassword(),
                referrerUserModel != null ? referrerUserModel.getLoginName() : null,
                dto.getChannel(),
                dto.getSource());
        UserModel newUserModel = registerUserService.register(registerDto);
        if (newUserModel != null) {
            dto.setLoginName(newUserModel.getLoginName());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId) {
        boolean returnValue = false;
        try {
            UserRestUserInfo userInfoResp = userRestClient.changePassword(new ChangePasswordRequestDto(loginName, originalPassword, newPassword));
            returnValue = userInfoResp.isSuccess();
            if (returnValue) {
                smsWrapperClient.sendPasswordChangedNotify(userInfoResp.getUserInfo().getMobile());
            }
        } catch (RestException e) {
            if (e.getStatus() != 401) {
                logger.error("change password failed", e);
            }
        }

        // 发送用户行为日志 MQ
        userOpLogService.sendUserOpLogMQ(loginName, ip, platform, deviceId, UserOpType.CHANGE_PASSWORD, returnValue ? "Success" : "Fail");
        return returnValue;
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
        return experienceAccountMapper.getExperienceBalance(loginName);
    }
}
