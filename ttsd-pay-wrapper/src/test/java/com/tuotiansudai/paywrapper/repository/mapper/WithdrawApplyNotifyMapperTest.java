package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.WithdrawApplyNotifyRequestModel;
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
public class WithdrawApplyNotifyMapperTest {

    @Autowired
    private WithdrawApplyNotifyMapper withdrawApplyNotifyMapper;

    @Test
    public void shouldCreateRechargeNotifyRequest() {
        WithdrawApplyNotifyRequestModel fakeRequest = this.getFakeRequest();

        withdrawApplyNotifyMapper.create(fakeRequest);

        assertNotNull(fakeRequest.getId());
    }

    private WithdrawApplyNotifyRequestModel getFakeRequest() {
        WithdrawApplyNotifyRequestModel model = new WithdrawApplyNotifyRequestModel();
        model.setService("service");
        model.setSignType("RSA");
        model.setSign("sign");
        model.setMerId("merId");
        model.setVersion("1.0");
        model.setOrderId("orderId");
        model.setMerDate("merDate");
        model.setTradeNo("tradeNo");
        model.setAmount("1");
        model.setRetCode("0000");
        model.setRetMsg("retMsg");
        model.setRequestData("requestData");

        return model;
    }
}
