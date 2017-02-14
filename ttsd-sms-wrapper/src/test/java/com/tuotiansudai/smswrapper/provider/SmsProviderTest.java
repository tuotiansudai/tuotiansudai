package com.tuotiansudai.smswrapper.provider;

import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SmsProviderTest {

    @Test
    @Ignore
    public void testSmsSendingNetease() throws SmsSendingException {
        SmsProviderNetease smsProviderNetease = new SmsProviderNetease();
        smsProviderNetease.setUrl("https://api.netease.im/sms/sendtemplate.action");
        smsProviderNetease.setAppKey("appkey");
        smsProviderNetease.setAppSecret("secret");
        List<String> mobileList = Arrays.asList("13800*****");
        List<String> paramList = Arrays.asList("123456");
        smsProviderNetease.sendSMS(mobileList, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, paramList);
    }

    @Test
    @Ignore
    public void testSmsSendingAlidayu() throws SmsSendingException {
        SmsProviderAlidayu smsProviderAlidayu = new SmsProviderAlidayu();
        smsProviderAlidayu.setUrl("http://gw.api.taobao.com/router/rest");
        smsProviderAlidayu.setAppKey("appkey");
        smsProviderAlidayu.setAppSecret("secret");
        smsProviderAlidayu.setSignName("拓天速贷");
        List<String> mobileList = Arrays.asList("13800******","18600******");
        List<String> paramList = Arrays.asList("a","b");
        smsProviderAlidayu.sendSMS(mobileList, SmsTemplate.SMS_COUPON_NOTIFY_TEMPLATE, paramList);
    }
}
