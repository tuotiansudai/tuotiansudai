package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JpushRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppJpushService;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MobileAppJpushServiceImpl implements MobileAppJpushService {

    static Logger log = Logger.getLogger(MobileAppJpushServiceImpl.class);
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";


    @Override
    public BaseResponseDto storeJpushId(JpushRequestDto jpushRequestDto) {
        String jpushId = jpushRequestDto.getJpushId();
        String platform = jpushRequestDto.getBaseParam().getPlatform();
        String value = platform == null ? jpushId : platform.toLowerCase() + "-" + jpushId;

        String loginName = jpushRequestDto.getBaseParam().getUserId();
        log.debug(MessageFormat.format("jpushId:{0} begin", value));
        redisWrapperClient.hset(JPUSH_ID_KEY, loginName, value);
        log.debug(MessageFormat.format("jpushId:{0} end", value));
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
