package com.tuotiansudai.smswrapper;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SmsTemplateTest {
    @Test
    public void shouldGenerateRegisterCaptchaContentPrimary() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("大宝剑").add("1001-01-01").build();
        String content = SmsTemplate.SMS_COUPON_NOTIFY_TEMPLATE.generateContent(paramList, SmsChannel.Primary);

        assertThat(content, is("尊敬的拓天速贷客户，恭喜您获得大宝剑，有效期1001-01-01，请及时投资体验。"));
    }

    @Test
    public void shouldGenerateRegisterCaptchaContentBackup() throws Exception {
        List<String> paramList = ImmutableList.<String>builder().add("大宝剑").add("1001-01-01").build();
        String content = SmsTemplate.SMS_COUPON_NOTIFY_TEMPLATE.generateContent(paramList, SmsChannel.Backup);

        assertThat(content, is("尊敬的拓天速贷客户，您专属的大宝剑已到账，有效期1001-01-01，请及时使用。"));
    }
}
