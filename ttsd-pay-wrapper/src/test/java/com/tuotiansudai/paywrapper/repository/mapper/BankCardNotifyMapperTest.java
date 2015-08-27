package com.tuotiansudai.paywrapper.repository.mapper;


import com.tuotiansudai.paywrapper.repository.model.async.callback.BankCardNotifyRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class BankCardNotifyMapperTest {

    @Autowired
    private BankCardNotifyMapper bankCardNotifyMapper;

    @Test
    public void shouldCreate() throws Exception {
        BankCardNotifyRequestModel model = new BankCardNotifyRequestModel();
        model.setUserId("userId");
        model.setLastFourCardid("1234");
        model.setGateId("ICBC");
        model.setService("mer_bind_card_notify");
        model.setSignType("RSA");
        model.setSign("sign");
        model.setMerId("merId");
        model.setVersion("4");
        model.setOrderId("orderId");
        model.setMerDate("20150505");
        model.setRetCode("0000");
        model.setCharset("utf-8");
        model.setRequestTime(new Date());
        model.setResponseTime(new Date());
        model.setRequestData("requestData");
        model.setResponseData("responseData");

        bankCardNotifyMapper.create(model);

        assertNotNull(model.getId());
    }
}
