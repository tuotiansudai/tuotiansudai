package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsCaptchaDto;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.utils.RequestIPParser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@Service
public class SmsCaptchaServiceImpl implements SmsCaptchaService {

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public boolean sendRegisterCaptcha(String mobile, String requestIP) {
        String captcha = this.createRegisterCaptcha(mobile);
        if (!Strings.isNullOrEmpty(captcha)) {
            BaseDto<BaseDataDto> resultDto = smsWrapperClient.sendRegisterCaptchaSms(new SmsCaptchaDto(mobile, captcha, requestIP));
            return resultDto.getData().getStatus();
        }

        return  false;
    }

    @Transactional(rollbackFor = Exception.class)
    private String createRegisterCaptcha(String mobile) {
        Date now = new Date();
        Date tenMinuteLater = new DateTime(now).plusMinutes(10).toDate();
        String captcha = createRandomCaptcha(6);

        SmsCaptchaModel existingCaptcha = smsCaptchaMapper.findByMobile(mobile);

        if (existingCaptcha != null) {
            existingCaptcha.setCaptcha(captcha);
            existingCaptcha.setExpiredTime(tenMinuteLater);
            existingCaptcha.setCreatedTime(now);
            smsCaptchaMapper.update(existingCaptcha);
        } else {
            SmsCaptchaModel newSmsCaptchaModel = new SmsCaptchaModel();
            newSmsCaptchaModel.setCaptcha(captcha);
            newSmsCaptchaModel.setMobile(mobile);
            newSmsCaptchaModel.setCreatedTime(now);
            newSmsCaptchaModel.setExpiredTime(tenMinuteLater);
            newSmsCaptchaModel.setCaptchaType(CaptchaType.REGISTER_CAPTCHA);
            smsCaptchaMapper.create(newSmsCaptchaModel);
        }

        return captcha;
    }

    @Override
    public boolean verifyRegisterCaptcha(String mobile, String captcha) {
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobile(mobile);
        Date now = new Date();
        return smsCaptchaModel != null && smsCaptchaModel.getCaptcha().equalsIgnoreCase(captcha) && smsCaptchaModel.getExpiredTime().after(now);
    }

    @Override
    public boolean sendRetrievePasswordCaptcha(String mobile, String requestIP) {
        String captcha = this.createRegisterCaptcha(mobile);
        if (!Strings.isNullOrEmpty(captcha)) {
            BaseDto<BaseDataDto> resultDto = smsWrapperClient.sendRetrievePasswordCaptchaSms(new SmsCaptchaDto(mobile, captcha, requestIP));
            return resultDto.getData().getStatus();
        }
        return  false;
    }

    @Transactional(rollbackFor = Exception.class)
    private String createMobileCaptcha(String mobile) {
        Date now = new Date();
        Date tenMinuteLater = new DateTime(now).plusMinutes(10).toDate();
        String captcha = createRandomCaptcha(6);
        SmsCaptchaModel existingCaptcha = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        if (existingCaptcha != null) {
            existingCaptcha.setCaptcha(captcha);
            existingCaptcha.setExpiredTime(tenMinuteLater);
            existingCaptcha.setCreatedTime(now);
            smsCaptchaMapper.update(existingCaptcha);
        } else {
            SmsCaptchaModel newSmsCaptchaModel = new SmsCaptchaModel();
            newSmsCaptchaModel.setCaptcha(captcha);
            newSmsCaptchaModel.setMobile(mobile);
            newSmsCaptchaModel.setCreatedTime(now);
            newSmsCaptchaModel.setExpiredTime(tenMinuteLater);
            newSmsCaptchaModel.setCaptchaType(CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
            smsCaptchaMapper.create(newSmsCaptchaModel);
        }
        return captcha;
    }

    @Override
    public boolean verifyMobileCaptcha(String mobile, String captcha) {
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobileAndCaptchaType(mobile, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        Date now = new Date();
        return smsCaptchaModel != null && smsCaptchaModel.getCaptcha().equalsIgnoreCase(captcha) && smsCaptchaModel.getExpiredTime().after(now);
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
