package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppJpushService;
import com.tuotiansudai.client.PushClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppJpushServiceImpl implements MobileAppJpushService {

    static Logger log = Logger.getLogger(MobileAppJpushServiceImpl.class);

    @Autowired
    private PushClient pushClient;

    @Override
    public BaseResponseDto storeJPushId(String loginName, String source, String jpushId) {
        pushClient.storeJPushId(loginName, source, jpushId);

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
