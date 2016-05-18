package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.service.UserOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOpLogServiceImpl implements UserOpLogService {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Override
    public void create(UserOpLogModel model) {
        userOpLogMapper.create(model);
    }
}
