package com.tuotiansudai.smswrapper;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SmsTemplateTest {
    @Test
    public void shouldGenerateRegisterCaptchaContentText() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("大宝剑").build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.getTemplateCellText().generateContent(paramList);

        assertThat(content, is("尊敬的拓天速贷客户，您的注册验证码是：大宝剑 。请勿泄露给他人！"));
    }
}
