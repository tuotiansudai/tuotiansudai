package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.CustWithdrawalsModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargePersonModel;
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
public class CustWithdrawalsMapperTest {

    @Autowired
    private CustWithdrawalsMapper custWithdrawalsMapper;

    @Test
    public void shouldCreateCustWithdrawalsModel() throws Exception {
        CustWithdrawalsModel model = new CustWithdrawalsModel("orderId", "umpUserId", "amount");
        model.setSign("sign");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        custWithdrawalsMapper.create(model);

        assertNotNull(model.getId());
    }
}
