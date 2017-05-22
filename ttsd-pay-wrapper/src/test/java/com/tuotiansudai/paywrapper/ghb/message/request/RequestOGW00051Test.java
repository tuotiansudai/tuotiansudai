package com.tuotiansudai.paywrapper.ghb.message.request;

import com.tuotiansudai.paywrapper.ghb.service.GHBMessageRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RequestOGW00051Test {

    @Autowired
    private GHBMessageRecordService ghbMessageRecordService;

    @Test
    public void name() throws Exception {
        RequestOGW00051 requestOGW00051 = new RequestOGW00051(1, 1, "loanName", 100, new Date(), "userName", "account");

        RequestMessageContent<RequestOGW00051> requestOGW00051RequestMessageContent = new RequestMessageContent<>(requestOGW00051);

        ghbMessageRecordService.saveRequestMessage(requestOGW00051RequestMessageContent);

        assertTrue(true);


    }
}
