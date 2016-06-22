package com.tuotiansudai.paywrapper.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class UMPayTransferBillServiceTest {

    @Autowired
    private UMPayTransferBillService umPayTransferBillService;

    @Test
    public void shouldName() throws Exception {
        umPayTransferBillService.getTransferBill("sidneygao", new Date(), new Date());

    }
}
