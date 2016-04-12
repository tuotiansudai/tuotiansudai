package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.repository.mapper.AccountMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PaySyncClientTest {

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    @Transactional
    public void test() throws Exception {
    }
}
