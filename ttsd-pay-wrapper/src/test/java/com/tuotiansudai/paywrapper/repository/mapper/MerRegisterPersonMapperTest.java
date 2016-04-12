package com.tuotiansudai.paywrapper.repository.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class MerRegisterPersonMapperTest {

    @Autowired
    private MerRegisterPersonMapper merRegisterPersonMapper;

    @Test
    public void shouldCreateMerRegisterPersonRequest() {
        MerRegisterPersonRequestModel model = new MerRegisterPersonRequestModel("orderId", "loginName", "userName", "identityNumber", "13900000000");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        merRegisterPersonMapper.createRequest(model);

        assertNotNull(model.getId());
    }

    @Test
    public void shouldUpdateMerRegisterPersonRequestStatus() {
        MerRegisterPersonRequestModel model = new MerRegisterPersonRequestModel("orderId", "loginName", "userName", "identityNumber", "13900000000");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        merRegisterPersonMapper.createRequest(model);

        merRegisterPersonMapper.updateRequestStatus(model.getId(), SyncRequestStatus.SUCCESS);

        BaseSyncRequestModel updatedModel = merRegisterPersonMapper.findRequestById(model.getId());

        assertThat(updatedModel.getStatus(), is(SyncRequestStatus.SUCCESS));
    }

    @Test
    public void shouldCreateMerRegisterPersonResponse() {
        MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel("orderId", "loginName", "userName", "identityNumber", "13900000000");
        requestModel.setSign("sign");
        requestModel.setRequestUrl("url");
        requestModel.setRequestData("requestData");
        merRegisterPersonMapper.createRequest(requestModel);

        MerRegisterPersonResponseModel responseModel = new MerRegisterPersonResponseModel();
        Map<String, String> fakeResponseData = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("ret_code", "ret_code")
                .put("ret_msg", "ret_msg")
                .put("user_id", "user_id")
                .put("account_id", "account_id")
                .put("reg_date", "reg_date")
                .build());

        responseModel.initializeModel(fakeResponseData);
        responseModel.setRequestId(requestModel.getId());
        merRegisterPersonMapper.createResponse(responseModel);

        assertNotNull(responseModel.getId());
    }
}
