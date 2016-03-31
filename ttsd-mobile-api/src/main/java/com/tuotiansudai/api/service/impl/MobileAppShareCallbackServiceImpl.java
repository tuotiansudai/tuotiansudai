package com.tuotiansudai.api.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.ShareCallbackRequestDataDto;
import com.tuotiansudai.api.dto.ShareType;
import com.tuotiansudai.api.service.MobileAppShareCallbackService;
import com.tuotiansudai.point.service.PointLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppShareCallbackServiceImpl implements MobileAppShareCallbackService {

    @Autowired
    private PointLotteryService pointLotteryService;

    @Override
    public boolean shareBannerSuccess(String loginName, ShareCallbackRequestDataDto requestDataDto) {
        if (!Strings.isNullOrEmpty(loginName) &&  requestDataDto.getShareType() == ShareType.BANNER && Integer.parseInt(requestDataDto.getObjectId()) == 1) {
            pointLotteryService.getLotteryOnceChance(loginName);
            return true;
        }
        return false;
    }
}
