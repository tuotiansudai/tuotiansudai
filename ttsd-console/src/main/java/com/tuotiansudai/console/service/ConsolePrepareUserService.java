package com.tuotiansudai.console.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.PrepareUserDto;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsolePrepareUserService {

    private static Logger logger = Logger.getLogger(ConsolePrepareUserService.class);

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private UserMapper userMapper;

    public List<PrepareUserDto> findPrepareUser(String mobile, Date beginTime, Date endTime, int index, int pageSize) {
        List<PrepareUserModel> prepareUserModels = prepareUserMapper.findPrepares(null, mobile, null, beginTime, endTime, (index - 1) * pageSize, pageSize);
        return fillPrepareUser(prepareUserModels);
    }

    public long findPrepareUserCount(String mobile, Date beginTime, Date endTime) {
        return prepareUserMapper.findPrepareCount(null, mobile, null, beginTime, endTime);
    }

    private List<PrepareUserDto> fillPrepareUser(List<PrepareUserModel> prepareUserModels) {
        return Lists.transform(prepareUserModels, prepareUserModel -> {
            UserModel referrerUserModel = userMapper.findByMobile(prepareUserModel.getReferrerMobile());
            UserModel useUserModel = userMapper.findByMobile(prepareUserModel.getMobile());
            return new PrepareUserDto(prepareUserModel, referrerUserModel, useUserModel);
        });
    }
}
