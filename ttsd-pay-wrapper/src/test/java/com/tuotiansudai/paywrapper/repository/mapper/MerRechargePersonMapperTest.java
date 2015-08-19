package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargePersonRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class MerRechargePersonMapperTest {

    @Autowired
    private MerRechargePersonMapper merRechargePersonMapper;

    @Test
    public void shouldCreateMerRegisterPersonRequest() {
        MerRechargePersonRequestModel model = new MerRechargePersonRequestModel("orderId", "umpUserId", "amount", "gateId");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        merRechargePersonMapper.createRequest(model);

        assertNotNull(model.getId());
    }
}
