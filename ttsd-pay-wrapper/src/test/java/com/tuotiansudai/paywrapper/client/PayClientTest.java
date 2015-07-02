package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.paywrapper.repository.model.MerRegisterPersonRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PayClientTest {

    @Autowired
    private PayClient payClient;

    @Test
    public void testName() throws Exception {
        MerRegisterPersonRequestModel registerRequestData = new MerRegisterPersonRequestModel("sidneygao", "高希端", "650102198104281210", "13810586920");
        Map<String, String> payRequestData = registerRequestData.generatePayRequestData();
        this.payClient.send(registerRequestData);
    }
}
