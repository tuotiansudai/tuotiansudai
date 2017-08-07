package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.mapper.HTrackingUserMapper;
import com.tuotiansudai.repository.model.HTrackingUserModel;
import com.tuotiansudai.service.HTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HTrackingServiceImpl implements HTrackingService {

    @Autowired
    private HTrackingUserMapper hTrackingUserMapper;

    @Override
    public void save(String mobile, String deviceId) {
        if (Strings.isNullOrEmpty(mobile) || Strings.isNullOrEmpty(deviceId)) {
            return;
        }

        if (hTrackingUserMapper.findByMobileAndDeviceId(mobile, deviceId) == null) {
            hTrackingUserMapper.create(new HTrackingUserModel(mobile, deviceId));
        }
    }
}
