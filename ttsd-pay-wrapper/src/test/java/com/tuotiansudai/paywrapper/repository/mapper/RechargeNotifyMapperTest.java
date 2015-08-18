package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackResponseModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.RechargeNotifyRequestModel;
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
public class RechargeNotifyMapperTest {

    @Autowired
    private RechargeNotifyMapper rechargeNotifyMapper;

    @Test
    public void shouldCreateRechargeNotifyRequest() {
        RechargeNotifyRequestModel fakeRequest = this.getFakeRequest();

        rechargeNotifyMapper.createRequest(fakeRequest);

        assertNotNull(fakeRequest.getId());
    }

    @Test
    public void shouldCreateRechargeNotifyResponse() throws Exception {
        RechargeNotifyRequestModel fakeRequest = this.getFakeRequest();
        rechargeNotifyMapper.createRequest(fakeRequest);

        BaseCallbackResponseModel responseModel = new BaseCallbackResponseModel(fakeRequest.getId(), fakeRequest.getOrderId(), fakeRequest.getMerDate());
        responseModel.setResponseData("responseData");

        rechargeNotifyMapper.createResponse(responseModel);

    }

    private RechargeNotifyRequestModel getFakeRequest() {
        RechargeNotifyRequestModel model = new RechargeNotifyRequestModel();
        model.setService("service");
        model.setSignType("RSA");
        model.setSign("sign");
        model.setMerId("merId");
        model.setVersion("1.0");
        model.setOrderId("orderId");
        model.setMerDate("merDate");
        model.setTradeNo("tradeNo");
        model.setBalance("balance");
        model.setComAmt("comAnt");
        model.setRetCode("0000");
        model.setRetMsg("retMsg");
        model.setRequestData("requestData");

        return model;
    }
}
