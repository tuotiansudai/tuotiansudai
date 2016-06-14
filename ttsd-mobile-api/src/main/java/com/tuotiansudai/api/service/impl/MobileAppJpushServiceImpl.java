package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JpushRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppJpushService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppJpushServiceImpl implements MobileAppJpushService {

    static Logger log = Logger.getLogger(MobileAppJpushServiceImpl.class);
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private JPushAlertService jPushAlertService;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    @Override
    public BaseResponseDto storeJPushId(JpushRequestDto jPushRequestDto) {
        String loginName = jPushRequestDto.getBaseParam().getUserId();
        String jPushId = jPushRequestDto.getJpushId();
        String platform = jPushRequestDto.getBaseParam().getPlatform();
        jPushAlertService.storeJPushId(loginName, platform, jPushId);

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
