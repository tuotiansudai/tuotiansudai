package com.tuotiansudai.paywrapper.service;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.dto.SendCloudType;
import com.tuotiansudai.paywrapper.service.impl.SendCloudMailServiceImpl;
import com.tuotiansudai.utils.AmountUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class SendCloudServiceTest {
    @InjectMocks
    private SendCloudMailServiceImpl sendCloudMailService;

    @Mock
    private SendCloudClient sendCloudClient;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldSendMailByRepayCompleted() throws UnsupportedEncodingException, MessagingException {
        Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                .put("loanName", "test")
                .put("periods", "2/3")
                .put("repayDate", "2015-05-05 12:12:12")
                .put("amount", "21.12")
                .build());

        boolean flag = sendCloudMailService.sendMailByRepayCompleted("aaa@ccc.com",emailParameters );
        assertTrue(flag);
    }

}
