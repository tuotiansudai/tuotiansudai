package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.JPushRequestDto;
import com.tuotiansudai.client.RedisWrapperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppJPushServiceTest{
    @Autowired
    private MobileAppJPushService mobileAppJPushService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Test
    public void shouldStoreJPushIdIsSuccess(){
        JPushRequestDto jPushRequestDto = new JPushRequestDto();
        String loginName = "test";
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        jPushRequestDto.setJpushId("1111");
        jPushRequestDto.setBaseParam(baseParam);
        mobileAppJPushService.storeJPushId(jPushRequestDto);
        String jpushId = redisWrapperClient.hget("api:jpushId:store",loginName);
        assertEquals("1111", jpushId);
        redisWrapperClient.del("api:jpushId:store");
    }
}
