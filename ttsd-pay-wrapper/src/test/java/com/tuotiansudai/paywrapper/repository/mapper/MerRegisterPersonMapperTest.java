package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.MerRegisterPersonRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration
@Transactional(value = "payTransactionManager")
public class MerRegisterPersonMapperTest {

    @Autowired
    private MerRegisterPersonMapper merRegisterPersonMapper;

    @Test
    public void shouldCreateMerRegisterPersonRequest() throws Exception {
        MerRegisterPersonRequestModel merRegisterPersonRequestModel = new MerRegisterPersonRequestModel("loginName", "userName", "identityNumber", "13900000000");
        merRegisterPersonRequestModel.setRequestUrl("url");

        merRegisterPersonMapper.createMerRegisterPersonRequest(merRegisterPersonRequestModel);

    }
}
