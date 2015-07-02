package com.tuotiansudai.smswrapper.service.impl;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.RegisterCaptchaMapper;
import com.tuotiansudai.smswrapper.repository.model.RegisterCaptchaModel;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private RegisterCaptchaMapper registerCaptchaMapper;

    @Override
    @Transactional
    public boolean sendRegisterCaptcha(String mobile, String captcha) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);
        String resultCode = smsClient.sendSMS(mobile, content);
        RegisterCaptchaModel model = new RegisterCaptchaModel(mobile, content, resultCode);
        registerCaptchaMapper.create(model);
        return !Strings.isNullOrEmpty(resultCode) && Long.parseLong(resultCode) > 0;
    }
}