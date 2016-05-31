package com.tuotiansudai.paywrapper.service;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.util.SendCloudMailUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class SendCloudServiceTest {
    @InjectMocks
    private SendCloudMailUtil sendCloudMailUtil;

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

        boolean flag = sendCloudMailUtil.sendMailByRepayCompleted("aaa@ccc.com",emailParameters );
        assertTrue(flag);
    }

    @Test
    public void shouldSendUserBalanceCheckingResult() throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", "2016-05-06 01:00:00");
        map.put("endTime", "2016-05-06 02:00:00");

        List<String> mismatchUserList = new ArrayList<>();
        mismatchUserList.add("zbx-199-200");
        mismatchUserList.add("zzz-199-200");
        mismatchUserList.add("aaa-199-200");
        mismatchUserList.add("vvvv-199-200");
        mismatchUserList.add("cccc-199-200");

        map.put("userList", mismatchUserList);

        boolean flag = sendCloudMailUtil.sendUserBalanceCheckingResult("zhoubaoxin@tuotiansudai.com", map);
        assertTrue(flag);
    }

}
