package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsCaptchaDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import com.tuotiansudai.service.SmsCaptchaService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class SmsCaptchaServiceImpl implements SmsCaptchaService {

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;
    @Autowired
    private SmsWrapperClient smsWrapperClient;

    private static final int CAPTCHA_LENGTH = 6;

    private static final int CAPTCHA_EXPIRED_TIME = 10;

    @Override
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, CaptchaType.TURN_OFF_NO_PASSWORD_INVEST);
        return smsWrapperClient.sendNoPasswordInvestCaptchaSms(new SmsCaptchaDto(mobile, captcha, requestIP));
    }

    @Override
    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, CaptchaType.REGISTER_CAPTCHA);
        return smsWrapperClient.sendRegisterCaptchaSms(new SmsCaptchaDto(mobile, captcha, requestIP));
    }

    @Override
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String requestIP) {
        String captcha = this.createMobileCaptcha(mobile, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        return smsWrapperClient.sendRetrievePasswordCaptchaSms(new SmsCaptchaDto(mobile, captcha, requestIP));
    }

    @Override
    public boolean verifyMobileCaptcha(String mobile, String captcha, CaptchaType captchaType) {
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, captchaType);
        Date now = new Date();
        return smsCaptchaModel != null && smsCaptchaModel.getCaptcha().equalsIgnoreCase(captcha) && smsCaptchaModel.getExpiredTime().after(now);
    }

    @Transactional(rollbackFor = Exception.class)
    private String createMobileCaptcha(String mobile, CaptchaType captchaType) {
        Date now = new Date();
        Date expiredTime = new DateTime(now).plusMinutes(CAPTCHA_EXPIRED_TIME).toDate();
        String captcha = createRandomCaptcha(CAPTCHA_LENGTH);
        SmsCaptchaModel existingCaptcha = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, captchaType);
        if (existingCaptcha != null) {
            existingCaptcha.setCaptcha(captcha);
            existingCaptcha.setExpiredTime(expiredTime);
            existingCaptcha.setCreatedTime(now);
            smsCaptchaMapper.update(existingCaptcha);
        } else {
            SmsCaptchaModel newSmsCaptchaModel = new SmsCaptchaModel();
            newSmsCaptchaModel.setCaptcha(captcha);
            newSmsCaptchaModel.setMobile(mobile);
            newSmsCaptchaModel.setCreatedTime(now);
            newSmsCaptchaModel.setExpiredTime(expiredTime);
            newSmsCaptchaModel.setCaptchaType(captchaType);
            smsCaptchaMapper.create(newSmsCaptchaModel);
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
