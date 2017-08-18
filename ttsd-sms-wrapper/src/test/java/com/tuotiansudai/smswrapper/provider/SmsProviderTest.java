package com.tuotiansudai.smswrapper.provider;

import com.google.common.collect.Lists;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import com.tuotiansudai.smswrapper.repository.mapper.CouponNotifyMapper;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsProviderTest {

    @Autowired
    @Qualifier("smsProviderNetease")
    private SmsProvider smsProviderNetease;

    @Autowired
    @Qualifier("smsProviderAlidayu")
    private SmsProvider smsProviderAlidayu;

    @Test
    public void testSmsSendingNetease() throws SmsSendingException {
        List<String> mobileList = Lists.newArrayList("13810586920");
        List<String> paramList = Lists.newArrayList("1234");
        smsProviderNetease.sendSMS(mobileList, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, paramList);
    }

    @Test
    public void testSmsSendingAlidayu() throws SmsSendingException {
        List<String> mobileList = Lists.newArrayList("13810586920", "13691070223");
        List<String> paramList = Lists.newArrayList("1234");
        smsProviderAlidayu.sendSMS(mobileList, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, paramList);
    }
}
