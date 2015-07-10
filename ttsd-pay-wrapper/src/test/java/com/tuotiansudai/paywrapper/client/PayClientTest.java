package com.tuotiansudai.paywrapper.client;

import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.model.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.response.MerRegisterPersonResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PayClientTest {

    @Autowired
    private PayClient payClient;

    @Test
    public void testName() throws Exception {
        MerRegisterPersonRequestModel registerRequestData = new MerRegisterPersonRequestModel("sidneygao", "高希端", "650102198104281210", "13810586920");
        MerRegisterPersonResponseModel response = payClient.send(MerRegisterPersonMapper.class, registerRequestData, MerRegisterPersonResponseModel.class);
    }
}
