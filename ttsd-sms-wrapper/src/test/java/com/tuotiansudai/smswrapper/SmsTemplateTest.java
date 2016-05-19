package com.tuotiansudai.smswrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SmsTemplateTest {

    @Test
    public void shouldGenerateRegisterCaptchaContent() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("1234").build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(paramList);

        assertThat(content, is("尊敬的拓天速贷客户，您的注册验证码是：1234 。请勿泄露给他人!【拓天速贷】"));
    }
}
