package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PaySyncClientTest {

    @Autowired
    private PaySyncClient paySyncClient;

    @Test
    @Transactional
    public void shouldRegisterPersonRequestFailed() throws Exception {
        MerRegisterPersonRequestModel registerRequestData = new MerRegisterPersonRequestModel("fakeLoginName", "fakeName", "fakeID", "13900000000");
        MerRegisterPersonResponseModel response = paySyncClient.send(MerRegisterPersonMapper.class, registerRequestData, MerRegisterPersonResponseModel.class);
        assertFalse(response.isSuccess());
    }
}
