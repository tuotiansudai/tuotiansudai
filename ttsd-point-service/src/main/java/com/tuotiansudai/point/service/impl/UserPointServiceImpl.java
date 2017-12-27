package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.UserPointModel;
import com.tuotiansudai.point.service.UserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserPointServiceImpl implements UserPointService {
    @Autowired
    private UserPointMapper userPointMapper;

    @Override
    public UserPointModel findOrCreate(String loginName) {
        UserPointModel model = userPointMapper.findByLoginName(loginName);
        if (model == null) {
            return createUserPointModel(loginName, 0, 0, null);
        }
        return model;
    }

    private UserPointModel createUserPointModel(String loginName, long sudaiPoint, long channelPoint, String channel) {
        UserPointModel model = new UserPointModel();
        model.setLoginName(loginName);
        model.setChannel(channel);
        model.setChannelPoint(channelPoint);
        model.setSudaiPoint(sudaiPoint);
        model.setPoint(sudaiPoint + channelPoint);
        model.setUpdatedTime(new Date());
        userPointMapper.create(model);
        return model;
    }
}
