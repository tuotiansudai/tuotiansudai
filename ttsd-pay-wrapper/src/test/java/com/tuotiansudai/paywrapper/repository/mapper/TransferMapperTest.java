package com.tuotiansudai.paywrapper.repository.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class TransferMapperTest {

    @Autowired
    private TransferMapper transferMapper;

    @Test
    public void shouldCreateTransferRequest() {
        TransferRequestModel model = TransferRequestModel.newRequest(String.valueOf(System.currentTimeMillis()), "payUserId", "1");
        model.setMerDate("2015-05-05");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        transferMapper.createRequest(model);

        assertNotNull(model.getId());
    }

    @Test
    public void shouldCreateTransferResponse(){
        TransferRequestModel model = TransferRequestModel.newRequest(String.valueOf(System.currentTimeMillis()), "payUserId", "1");
        model.setMerDate("2015-05-05");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        transferMapper.createRequest(model);
        TransferResponseModel responseModel = new TransferResponseModel();
        Map<String, String> fakeResponseData = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("ret_code", "ret_code")
                .put("ret_msg", "ret_msg")
                .put("mer_date", "2005-05-05")
                .put("mer_check_date", "2015-09-09")
                .put("response_time", "2009-09-09")
                .put("response_data", "response_data")
                .put("order_id", "123456")
                .put("trade_no", "123456789")
                .build());

        responseModel.initializeModel(fakeResponseData);
        responseModel.setRequestId(model.getId());
        transferMapper.createResponse(responseModel);
        assertNotNull(responseModel.getId());
    }
}
