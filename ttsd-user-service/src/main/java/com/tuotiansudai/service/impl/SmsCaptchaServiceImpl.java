package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import com.tuotiansudai.service.SmsCaptchaService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class SmsCaptchaServiceImpl implements SmsCaptchaService {

    private static final int CAPTCHA_LENGTH = 6;

    private static final int CAPTCHA_EXPIRED_TIME = 10;


    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;
    @Value("${default.mobile.captcha}")
    private String defaultMobileCaptha;

    @Override
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, boolean isVoice, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, SmsCaptchaType.NO_PASSWORD_INVEST);
        return sendSms(JianZhouSmsTemplate.SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE, mobile, captcha, isVoice, requestIP);
    }

    @Override
    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, boolean isVoice, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, SmsCaptchaType.REGISTER_CAPTCHA);
        return sendSms(JianZhouSmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, mobile, captcha, isVoice, requestIP);
    }

    @Override
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, boolean isVoice, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, SmsCaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        return sendSms(JianZhouSmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE, mobile, captcha, isVoice, requestIP);
    }

    private BaseDto<SmsDataDto> sendSms(JianZhouSmsTemplate jianZhouSmsTemplate, String mobile, String params, boolean isVoice, String requestIP){
        SmsNotifyDto smsNotifyDto = new SmsNotifyDto(jianZhouSmsTemplate, Lists.newArrayList(mobile), Lists.newArrayList(params), isVoice, requestIP);
        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, smsNotifyDto);
        SmsDataDto smsDataDto = new SmsDataDto();
        smsDataDto.setStatus(true);
        smsDataDto.setIsRestricted(false);
        return new BaseDto<>(smsDataDto);
    }

    @Override
    public boolean verifyMobileCaptcha(String mobile, String captcha, SmsCaptchaType smsCaptchaType) {
        if(!Environment.isProduction(environment) && defaultMobileCaptha!=null && defaultMobileCaptha.equals(captcha)){
            return true;
        }
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, smsCaptchaType);
        Date now = new Date();
        return smsCaptchaModel != null && smsCaptchaModel.getCaptcha().equalsIgnoreCase(captcha) && smsCaptchaModel.getExpiredTime().after(now);
    }

    private String createMobileCaptcha(String mobile, SmsCaptchaType smsCaptchaType) {
        DateTime now = new DateTime();
        DateTime expiredTime = now.plusMinutes(CAPTCHA_EXPIRED_TIME);
        String captcha = createRandomCaptcha(CAPTCHA_LENGTH);
        SmsCaptchaModel existingCaptcha = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, smsCaptchaType);
        if (existingCaptcha != null) {
            existingCaptcha.setCaptcha(captcha);
            existingCaptcha.setExpiredTime(expiredTime.toDate());
            existingCaptcha.setCreatedTime(new Date());
            smsCaptchaMapper.update(existingCaptcha);
        } else {
            smsCaptchaMapper.create(new SmsCaptchaModel(captcha, mobile, expiredTime.toDate(), smsCaptchaType));
        }
        return captcha;
    }

    private String createRandomCaptcha(int captchaLength) {
        int min = 0;
        int max = 9;
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < captchaLength; i++) {
            captcha.append(String.valueOf(random.nextInt(max - min + 1) + min));
        }
        return captcha.toString();
    }
}
