package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.PrepareUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PrepareUserServiceImpl implements PrepareUserService {

    private static Logger logger = Logger.getLogger(PrepareUserServiceImpl.class);

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Override
    @Transactional
    public BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto) {

        boolean referrerMobileIsExist = userService.mobileIsExist(requestDto.getReferrerMobile());
        boolean mobileIsExist = userService.mobileIsExist(requestDto.getMobile());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyMobileCaptcha(requestDto.getMobile(), requestDto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);
        boolean prepareUserExist = prepareUserMapper.findByMobile(requestDto.getMobile()) != null;

        if (!referrerMobileIsExist || mobileIsExist || verifyCaptchaFailed || prepareUserExist) {
            return new BaseDataDto(false, null);
        }

        try {
            UserModel userModel = userService.findByMobile(requestDto.getReferrerMobile());
            PrepareUserModel prepareUserModel = new PrepareUserModel();
            prepareUserModel.setReferrerLoginName(userModel != null ? userModel.getLoginName() : null);
            prepareUserModel.setReferrerMobile(requestDto.getReferrerMobile());
            prepareUserModel.setMobile(requestDto.getMobile());
            prepareUserModel.setCreatedTime(new Date());
            prepareUserModel.setChannel(Source.IOS);
            prepareUserModel.setRegisterChannel(UserChannel.APP_SHARE);
            prepareUserMapper.create(prepareUserModel);
            return new BaseDataDto(true, null);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getMessage());
        }
    }

    @Override
    @Transactional
    public BaseDataDto register(RegisterUserDto requestDto) {
        boolean isRegisterSuccess = userService.registerUser(requestDto);
        return new BaseDataDto(isRegisterSuccess, null);
    }
}
