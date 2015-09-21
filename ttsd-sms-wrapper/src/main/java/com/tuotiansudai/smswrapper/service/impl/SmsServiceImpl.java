package com.tuotiansudai.smswrapper.service.impl;


import com.google.common.collect.ImmutableMap;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.RetrievePasswordCaptchaMapper;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsClient smsClient;

    @Override
    public boolean sendRegisterCaptcha(String mobile, String captcha, String ip) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(RegisterCaptchaMapper.class, mobile, content, true, ip);
    }

    @Override
    public boolean sendRetrievePasswordCaptcha(String mobile, String captcha, String ip) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(RetrievePasswordCaptchaMapper.class, mobile, content, true, ip);
    }
}