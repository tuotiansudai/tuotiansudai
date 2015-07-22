package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.SmsClient;
import com.tuotiansudai.client.dto.ResultDto;
import com.tuotiansudai.repository.mapper.SmsCaptchaMapper;
import com.tuotiansudai.repository.model.CaptchaStatus;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import com.tuotiansudai.service.SmsCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsCaptchaServiceImpl implements SmsCaptchaService {

    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Autowired
    private SmsClient smsClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendSmsByMobileNumberRegister(String mobileNumber) {
        //校验手机号的有效性
        if (!this.verifyMobileNumber(mobileNumber)) {
            return false;
        }
        //创建验证码
        SmsCaptchaModel smsCaptchaModel = this.createSmsCaptcha(mobileNumber);

        String captcha = smsCaptchaModel.getCode();
        if (!Strings.isNullOrEmpty(captcha)) {
            ResultDto resultDto = smsClient.sendSms(mobileNumber, captcha);
            return resultDto.getData().getStatus();
        }

        return  false;
    }

    private boolean verifyMobileNumber(String mobileNumber) {
        if (StringUtils.isEmpty(mobileNumber)) {
            return false;
        }
        if (mobileNumber.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(mobileNumber);

        return matcher.matches();
    }

    @Transactional(rollbackFor = Exception.class)
    private SmsCaptchaModel createSmsCaptcha(String mobileNumber) {
        int delayedMinute = 10;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, delayedMinute);

        SmsCaptchaModel smsCaptchaModelUpdate = smsCaptchaMapper.findRegisterCaptchaByMobile(mobileNumber);
        if (smsCaptchaModelUpdate != null) {
            smsCaptchaModelUpdate.setCode(createRandomCaptcha(6));
            smsCaptchaModelUpdate.setDeadLine(cal.getTime());
            smsCaptchaModelUpdate.setGenerationTime(new Date());
            smsCaptchaMapper.updateSmsCaptchaByMobile(smsCaptchaModelUpdate);
            return smsCaptchaModelUpdate;
        } else {
            SmsCaptchaModel smsCaptchaModelNew = new SmsCaptchaModel();
            smsCaptchaModelNew.setCode(createRandomCaptcha(6));
            smsCaptchaModelNew.setMobile(mobileNumber);
            smsCaptchaModelNew.setGenerationTime(new Date());
            smsCaptchaModelNew.setDeadLine(cal.getTime());
            smsCaptchaModelNew.setStatus(CaptchaStatus.ACTIVE);
            smsCaptchaModelNew.setCaptchaType(CaptchaType.REGISTERCAPTCHA);
            smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModelNew);
            return smsCaptchaModelNew;
        }

    }

    @Override
    public boolean verifyCaptcha(String mobile, String code) {
        SmsCaptchaModel smsCaptchaModelQuery = new SmsCaptchaModel();
        smsCaptchaModelQuery.setCode(code);
        smsCaptchaModelQuery.setMobile(mobile);
        smsCaptchaModelQuery.setCaptchaType(CaptchaType.REGISTERCAPTCHA);
        smsCaptchaModelQuery.setStatus(CaptchaStatus.ACTIVE);

        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModelQuery);
        //校验手机验证码存在
        Date now = new Date();
        return smsCaptchaModel != null && smsCaptchaModel.getDeadLine().after(now);
    }

    private String createRandomCaptcha(Integer captchaLength) {
        //验证码
        String captcha = "";
        for (int i = 0; i < captchaLength; i++) {
            captcha = captcha + (int) (Math.random() * 9);
        }
        return captcha;
    }


}
