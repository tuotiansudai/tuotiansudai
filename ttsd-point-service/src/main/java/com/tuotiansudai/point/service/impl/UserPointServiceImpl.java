package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.UserPointModel;
import com.tuotiansudai.point.service.UserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPointServiceImpl implements UserPointService {
    @Autowired
    private UserPointMapper userPointMapper;

    @Override
    public UserPointModel findOrCreateWithLock(String loginName) {
        if (!userPointMapper.exists(loginName)) {
            userPointMapper.createIfNotExist(new UserPointModel(loginName, 0, 0, null));
        }
        return userPointMapper.lockByLoginName(loginName);
    }
}
