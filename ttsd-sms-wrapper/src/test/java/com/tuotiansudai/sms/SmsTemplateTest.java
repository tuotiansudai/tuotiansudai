package com.tuotiansudai.sms;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SmsTemplateTest {

    @Test
    public void shouldGenerateRegisterCaptchaContent() throws Exception {
        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("captcha", "1234")
                .build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);

        assertThat(content, is("尊敬的用户，您的注册验证码是：1234 。请勿泄露给他人!【拓天速贷】"));
    }
}
