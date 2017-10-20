package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotWorkService {

    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    UserMapper userMapper;

    final private long[] prizeList = {300000L, 800000L, 3000000L, 5000000L, 10000000L, 20000000L, 30000000L, 52000000L,
            80000000L, 120000000L};

    public long getUsersActivityInvestAmount(String loginName) {
        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            return 0L;
        } else {
            return notWorkModel.getInvestAmount();
        }
    }

    public long getUsersNeedInvestAmount(String loginName) {
        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            return prizeList[0];
        }
        for (long prize : prizeList) {
            if (prize > notWorkModel.getInvestAmount()) {
                return prize - notWorkModel.getInvestAmount();
            }
        }
        return 0;
    }
}
