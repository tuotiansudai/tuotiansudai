package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PrepareMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.PrepareService;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PrepareServiceImpl implements PrepareService {

    private static Logger logger = Logger.getLogger(PrepareServiceImpl.class);

    @Autowired
    private PrepareMapper prepareMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto) {
        boolean referrerMobileIsExist = userService.mobileIsExist(requestDto.getReferrerMobile());
        boolean mobileIsExist = userService.mobileIsExist(requestDto.getMobile());

        if (!referrerMobileIsExist || mobileIsExist) {
            return new BaseDataDto(false, null);
        }

        try {
            PrepareModel prepareModel = new PrepareModel();
            prepareModel.setReferrerMobile(requestDto.getReferrerMobile());
            prepareModel.setMobile(requestDto.getMobile());
            requestDto.setChannel(requestDto.getChannel());
            prepareMapper.create(prepareModel);
            return new BaseDataDto(true, null);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getMessage());
        }
    }

    @Override
    public List<PrepareUserDto> findPrepareUser(String mobile, Date beginTime, Date endTime, int index, int pageSize) {
        List<PrepareModel> prepareModels = prepareMapper.findPrepares(null, mobile, null, beginTime, endTime, (index - 1) * pageSize, pageSize);
        return fillPrepareUser(prepareModels);
    }

    @Override
    public long findPrepareUserCount(String mobile, Date beginTime, Date endTime) {
        return prepareMapper.findPrepareCount(null, mobile, null, beginTime, endTime);
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

    public BaseDataDto register(ActivityRegisterRequestDto requestDto) {
        RegisterUserDto userDto = requestDto.convertToRegisterUserDto();
        try {
            boolean isRegisterSuccess = userService.registerUser(userDto);
            return new BaseDataDto(isRegisterSuccess, null);
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage());
            return new BaseDataDto(false, e.getLocalizedMessage());
        }
    }

}
