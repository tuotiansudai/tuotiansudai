package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JPushRequestDto;
import com.tuotiansudai.api.dto.JPushRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppJPushService;
import com.tuotiansudai.api.service.MobileAppJPushService;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MobileAppJPushServiceImpl implements MobileAppJPushService {

    static Logger log = Logger.getLogger(MobileAppJPushServiceImpl.class);
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";


    @Override
    public BaseResponseDto storeJPushId(JPushRequestDto jPushRequestDto) {
        String jPushId = jPushRequestDto.getJpushId();
        String loginName = jPushRequestDto.getBaseParam().getUserId();
        log.debug(MessageFormat.format("jpushId:{0} begin", jPushId));
        redisWrapperClient.hset(JPUSH_ID_KEY, loginName, jPushId);
        log.debug(MessageFormat.format("jpushId:{0} end", jPushId));
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
