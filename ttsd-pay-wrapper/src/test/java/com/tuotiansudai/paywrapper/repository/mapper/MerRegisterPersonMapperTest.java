package com.tuotiansudai.paywrapper.repository.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.repository.model.request.BaseRequestModel;
import com.tuotiansudai.paywrapper.repository.model.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.request.RequestStatus;
import com.tuotiansudai.paywrapper.repository.model.response.MerRegisterPersonResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration
@Transactional(value = "payTransactionManager")
public class MerRegisterPersonMapperTest {

    @Autowired
    private MerRegisterPersonMapper merRegisterPersonMapper;

    @Test
    public void shouldCreateMerRegisterPersonRequest() {
        MerRegisterPersonRequestModel model = new MerRegisterPersonRequestModel("loginName", "userName", "identityNumber", "13900000000");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        merRegisterPersonMapper.createRequest(model);

        assertNotNull(model.getId());
    }

    @Test
    public void shouldUpdateMerRegisterPersonRequestStatus() {
        MerRegisterPersonRequestModel model = new MerRegisterPersonRequestModel("loginName", "userName", "identityNumber", "13900000000");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        merRegisterPersonMapper.createRequest(model);

        merRegisterPersonMapper.updateRequestStatus(model.getId(), RequestStatus.SUCCESS);

        BaseRequestModel updatedModel = merRegisterPersonMapper.findRequestById(model.getId());

        assertThat(updatedModel.getStatus(), is(RequestStatus.SUCCESS));
    }

    @Test
    public void shouldCreateMerRegisterPersonResponse() {
        MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel("loginName", "userName", "identityNumber", "13900000000");
        requestModel.setRequestUrl("url");
        requestModel.setRequestData("requestData");
        merRegisterPersonMapper.createRequest(requestModel);

        MerRegisterPersonResponseModel responseModel = new MerRegisterPersonResponseModel();
        Map<String, String> fakeResponseData = Maps.newHashMap(ImmutableMap.<String, String>builder()
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
