package com.tuotiansudai.client;

import com.tuotiansudai.dto.SendCloudType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SendCloudWrapperClientTest {



    @Autowired
    private SendCloudClient sendCloudClient;

    @Test
    public void shouldSendCloudIsOk() {
        try {
            sendCloudClient.sendMailBySendCloud("zhanglong@tuotiansudai.com","标的放款成功邮件提醒","绑定邮箱，认证码：%authCode%", SendCloudType.CONTENT);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}
