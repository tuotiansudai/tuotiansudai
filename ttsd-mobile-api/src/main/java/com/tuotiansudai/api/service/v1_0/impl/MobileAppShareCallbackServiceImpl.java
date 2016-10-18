package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v1_0.ShareCallbackRequestDataDto;
import com.tuotiansudai.api.dto.v1_0.ShareType;
import com.tuotiansudai.api.service.v1_0.MobileAppShareCallbackService;
import com.tuotiansudai.point.service.PointLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppShareCallbackServiceImpl implements MobileAppShareCallbackService {

    @Autowired
    private PointLotteryService pointLotteryService;

    @Override
    public boolean shareBannerSuccess(String loginName, ShareCallbackRequestDataDto requestDataDto) {
        if (!Strings.isNullOrEmpty(loginName) && requestDataDto.getShareType() == ShareType.BANNER && "1".equals(requestDataDto.getObjectId())) {
            pointLotteryService.getLotteryOnceChance(loginName);
            return true;
        }
        return false;
    }
}
