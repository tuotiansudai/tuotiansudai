package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.dto.request.ChangePasswordRequestDto;
import com.tuotiansudai.dto.request.RegisterRequestDto;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.repository.mapper.ExperienceAccountMapper;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.service.RegisterUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

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
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private RegisterUserService registerUserService;

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
            logger.warn(MessageFormat.format("[Register User {0}] referrer({1}) is not existed", dto.getMobile(), referrer));
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
        String referrer = referrerUserModel != null ? referrerUserModel.getLoginName() : null;
        RegisterRequestDto registerDto = new RegisterRequestDto(
                dto.getMobile(),
                dto.getPassword(),
                referrer,
                dto.getChannel(),
                dto.getSource(),
                Strings.isNullOrEmpty(dto.getActivityReferrer()) ? null : referrer);
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
                mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE, Lists.newArrayList(userInfoResp.getUserInfo().getMobile())));
            }
        } catch (RestException e) {
            if (e.getStatus() != 401) {
                logger.error("change password failed", e);
            }
        }

        // 发送用户行为日志 MQ
        String mobile= Optional.ofNullable(userMapper.findByLoginName(loginName)).orElse(new UserModel()).getMobile();
        UserOpLogMessage userOpLogMessage=new UserOpLogMessage(IdGenerator.generate(),loginName,mobile,UserOpType.CHANGE_PASSWORD,ip,deviceId,platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)),returnValue ? "Success" : "Fail");
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(userOpLogMessage));
        } catch (JsonProcessingException e) {
            logger.error("[UserServiceImpl] " +"changePassword"+ ", send UserOperateLog fail.", e);
        }
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
