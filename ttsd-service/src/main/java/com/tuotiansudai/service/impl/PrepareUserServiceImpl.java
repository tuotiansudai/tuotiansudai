package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.PrepareUserDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.PrepareUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PrepareUserServiceImpl implements PrepareUserService {

    private static Logger logger = Logger.getLogger(PrepareUserServiceImpl.class);

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

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

        if (!referrerMobileIsExist || mobileIsExist || verifyCaptchaFailed) {
            return new BaseDataDto(false, null);
        }

        try {
            PrepareModel prepareModel = new PrepareModel();
            prepareModel.setReferrerMobile(requestDto.getReferrerMobile());
            prepareModel.setMobile(requestDto.getMobile());
            prepareModel.setChannel(requestDto.getChannel());
            prepareModel.setCreatedTime(new Date());
            prepareUserMapper.create(prepareModel);
            return new BaseDataDto(true, null);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getMessage());
        }
    }

    @Override
    public List<PrepareUserDto> findPrepareUser(String mobile, Date beginTime, Date endTime, int index, int pageSize) {
        List<PrepareModel> prepareModels = prepareUserMapper.findPrepares(null, mobile, null, beginTime, endTime, (index - 1) * pageSize, pageSize);
        return fillPrepareUser(prepareModels);
    }

    @Override
    public long findPrepareUserCount(String mobile, Date beginTime, Date endTime) {
        return prepareUserMapper.findPrepareCount(null, mobile, null, beginTime, endTime);
    }

    private List<PrepareUserDto> fillPrepareUser(List<PrepareModel> prepareModels) {
        return Lists.transform(prepareModels, new Function<PrepareModel, PrepareUserDto>() {
            @Override
            public PrepareUserDto apply(PrepareModel prepareModel) {
                UserModel referrerUserModel = userMapper.findByMobile(prepareModel.getReferrerMobile());
                AccountModel referrerAccountModel = accountMapper.findByLoginName(referrerUserModel.getLoginName());
                UserModel useUserModel = userMapper.findByMobile(prepareModel.getMobile());
                return new PrepareUserDto(prepareModel, referrerAccountModel, useUserModel);
            }
        });
    }

    @Override
    @Transactional
    public BaseDataDto register(RegisterUserDto requestDto) {
        try {
            boolean isRegisterSuccess = userService.registerUser(requestDto);
            return new BaseDataDto(isRegisterSuccess, null);
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getLocalizedMessage());
        }
    }

}
