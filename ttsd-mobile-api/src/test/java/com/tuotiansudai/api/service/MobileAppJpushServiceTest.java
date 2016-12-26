package com.tuotiansudai.api.service;


import com.tuotiansudai.api.service.v1_0.MobileAppJpushService;
import com.tuotiansudai.client.RedisWrapperClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertEquals;

public class MobileAppJpushServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppJpushService mobileAppJpushService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Test
    public void shouldStoreJPushIdIsSuccess() {
        mobileAppJpushService.storeJPushId("loginName", "ios", "jpushId");
        String jpushId = redisWrapperClient.hget("api:jpushId:store", "loginName");
        assertEquals("ios-jpushId", jpushId);
    }
}
