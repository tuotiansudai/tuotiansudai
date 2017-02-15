package com.tuotiansudai.smswrapper;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SmsTemplateTest {
    @Test
    public void shouldGenerateRegisterCaptchaContentPrimary() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("大宝剑").build();
        String content = SmsTemplate.SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE.generateContent(paramList, SmsChannel.Primary);

        assertThat(content, is("尊敬的用户，恭喜您获得了一张大宝剑，请尽快使用拿奖励哦！"));
    }

    @Test
    public void shouldGenerateRegisterCaptchaContentBackup() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("大宝剑").build();
        String content = SmsTemplate.SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE.generateContent(paramList, SmsChannel.Backup);

        assertThat(content, is("尊敬的用户，恭喜您获得了一张大宝剑，请尽快使用拿奖励哦！"));
    }
}
