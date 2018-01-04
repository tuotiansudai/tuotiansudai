package com.tuotiansudai.smswrapper.provider;

import com.google.common.collect.Lists;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsProviderTest {

    @Autowired
    @Qualifier("smsProviderAlidayuText")
    private SmsProvider smsProviderAlidayuText;


    @Autowired
    @Qualifier("smsProviderAlidayuVoice")
    private SmsProvider smsProviderAlidayuVoice;

    @Test
    @Ignore
    public void testSmsSendingAlidayuText() throws SmsSendingException {
        List<String> mobileList = Lists.newArrayList("18611445119");
        List<String> paramList = Lists.newArrayList("1234");
        smsProviderAlidayuText.sendSMS(mobileList, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, paramList);
    }

    @Test
    @Ignore
    public void testSmsSendingAlidayuVoice() throws SmsSendingException {
        List<String> mobileList = Lists.newArrayList("18611445119");
        List<String> paramList = Lists.newArrayList("1234");
        smsProviderAlidayuVoice.sendSMS(mobileList, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, paramList);
    }
}
