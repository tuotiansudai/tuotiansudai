package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.PrepareUserDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PrepareMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.PrepareService;
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

    @Override
    public BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto) {
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
}
