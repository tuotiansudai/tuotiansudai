package com.tuotiansudai.smswrapper.service.impl;


import com.google.common.collect.ImmutableMap;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.InvestNotifyMapper;
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
    public boolean sendRegisterCaptcha(String mobile, String captcha) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(RegisterCaptchaMapper.class, mobile, content);
    }

    @Override
    public boolean sendInvestNotify(InvestSmsNotifyDto dto) {
        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("loanName", dto.getLoanName())
                .put("amount", dto.getAmount())
                .build();
        String content = SmsTemplate.SMS_INVEST_NOTIFY_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(InvestNotifyMapper.class, dto.getMobile(), content);
    }
}