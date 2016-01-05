package com.tuotiansudai.smswrapper.service.impl;


import com.google.common.collect.ImmutableMap;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.SmsCouponNotifyDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.*;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsClient smsClient;

    @Override
    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, String ip) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(RegisterCaptchaMapper.class, mobile, content, ip);
    }

    @Override
    public BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto) {
        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("loanName", dto.getLoanName())
                .put("amount", dto.getAmount())
                .build();
        String content = SmsTemplate.SMS_INVEST_NOTIFY_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(InvestNotifyMapper.class, dto.getMobile(), content, "");
    }

    @Override
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, String ip) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("captcha", captcha).build();
        String content = SmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(RetrievePasswordCaptchaMapper.class, mobile, content, ip);
    }

    @Override
    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile) {
        String content = SmsTemplate.SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE.generateContent(new HashMap<String,String>(0));
        return smsClient.sendSMS(UserPasswordChangedNotifyMapper.class, mobile, content, "");
    }

    @Override
    public BaseDto<SmsDataDto> investFatalNotify(String mobile, String errMsg) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("errMsg", errMsg).build();
        String content = SmsTemplate.SMS_INVEST_FATAL_NOTIFY_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(InvestFatalNotifyMapper.class, mobile, content, "");
    }

    @Override
    public BaseDto<SmsDataDto> jobFatalNotify(String mobile, String errMsg) {
        Map<String, String> map = ImmutableMap.<String, String>builder().put("errMsg", errMsg).build();
        String content = SmsTemplate.SMS_JOB_FATAL_NOTIFY_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(JobFatalNotifyMapper.class, mobile, content, "");
    }

    @Override
    public BaseDto<SmsDataDto> couponNotify(SmsCouponNotifyDto notifyDto) {
        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("amount", notifyDto.getAmount())
                .put("couponType", notifyDto.getCouponType().getDesc())
                .put("expiredDate", new DateTime(notifyDto.getExpiredDate()).toString("yyyy-MM-dd"))
                .build();
        String content = SmsTemplate.SMS_COUPON_NOTIFY_TEMPLATE.generateContent(map);
        return smsClient.sendSMS(JobFatalNotifyMapper.class, notifyDto.getMobile(), content, "");
    }
}