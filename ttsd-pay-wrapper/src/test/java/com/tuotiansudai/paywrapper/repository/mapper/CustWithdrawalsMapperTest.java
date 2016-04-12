package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.CustWithdrawalsRequestModel;
import com.tuotiansudai.repository.model.Source;
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
        CustWithdrawalsRequestModel model = new CustWithdrawalsRequestModel("orderId", "umpUserId", "amount", Source.WEB);
        model.setSign("sign");
        model.setRetUrl("ret_url");
        model.setNotifyUrl("notify_url");
        model.setRequestUrl("url");
        model.setRequestData("requestData");

        custWithdrawalsMapper.create(model);

        assertNotNull(model.getId());
    }
}
