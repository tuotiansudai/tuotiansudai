package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.JpushRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppJpushService;
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
public class MobileAppJpushServiceTest{
    @Autowired
    private MobileAppJpushService mobileAppJpushService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Test
    public void shouldStoreJpushIdIsSuccess(){
        JpushRequestDto jpushRequestDto = new JpushRequestDto();
        String loginName = "test";
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        jpushRequestDto.setJpushId("1111");
        jpushRequestDto.setBaseParam(baseParam);
        mobileAppJpushService.storeJpushId(jpushRequestDto);
        String jpushId = redisWrapperClient.hget("api:jpushId:store",loginName);
        assertEquals("1111",jpushId);
        redisWrapperClient.del("api:jpushId:store");
    }
}
