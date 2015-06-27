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
    public boolean sendSmsbyMobileNumberRegister(String mobileNumber) {
        //校验手机号的有效性
        if (!this.verifyMobileNumber(mobileNumber)) {
            return false;
        }
        //创建验证码
        SmsCaptchaModel smsCaptchaModel = this.createSmsCaptcha(mobileNumber);

        //发送短信
        String captcha = smsCaptchaModel.getCode();

        return !Strings.isNullOrEmpty(captcha) && this.sendRegisterCaptcha(captcha, mobileNumber);
    }

    @Override
    public boolean verifyMobileNumber(String mobileNumber) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SmsCaptchaModel createSmsCaptcha(String mobileNumber) {
        int delayedMinute = 10;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, delayedMinute);

        SmsCaptchaModel smsCaptchaModelOld = new SmsCaptchaModel();
        smsCaptchaModelOld.setMobile(mobileNumber);
        smsCaptchaModelOld.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModelOld.setStatus(CaptchaStatus.ACTIVATED);
        SmsCaptchaModel smsCaptchaModelUpdate = smsCaptchaMapper.findCaptchabyMobile(smsCaptchaModelOld);
        if (smsCaptchaModelUpdate != null) {
            smsCaptchaModelUpdate.setCode(createRandomVcode(6));
            smsCaptchaModelUpdate.setDeadLine(cal.getTime());
            smsCaptchaModelUpdate.setGenerationTime(new Date());
            smsCaptchaMapper.updateSmsCaptchaByMobile(smsCaptchaModelUpdate);
            return smsCaptchaModelUpdate;
        } else {
            SmsCaptchaModel smsCaptchaModelNew = new SmsCaptchaModel();
            smsCaptchaModelNew.setCode(createRandomVcode(6));
            smsCaptchaModelNew.setMobile(mobileNumber);
            smsCaptchaModelNew.setGenerationTime(new Date());
            smsCaptchaModelNew.setDeadLine(cal.getTime());
            smsCaptchaModelNew.setStatus(CaptchaStatus.ACTIVATED);
            smsCaptchaModelNew.setCaptchaType(CaptchaType.MOBILECAPTCHA);
            smsCaptchaModelNew.setUserId(null);
            smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModelNew);
            return smsCaptchaModelNew;
        }

    }

    @Override
    public boolean verifyCaptcha(String mobile, String code) {
        SmsCaptchaModel smsCaptchaModelQuery = new SmsCaptchaModel();
        smsCaptchaModelQuery.setCode(code);
        smsCaptchaModelQuery.setMobile(mobile);
        smsCaptchaModelQuery.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModelQuery.setStatus(CaptchaStatus.ACTIVATED);

        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModelQuery);
        //校验手机验证码存在
        if (smsCaptchaModel == null) {
            return false;
        }
        //校验验证码是否失效
        if (smsCaptchaModel.getDeadLine() != null && smsCaptchaModel.getDeadLine().before(new Date())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean sendRegisterCaptcha(String mobile, String captcha) {
        ResultDto resultDto = smsClient.sendSms(mobile, captcha);
        return resultDto != null && resultDto.getData().getStatus();
    }

    public  String createRandomVcode(Integer captchaLength){
        //验证码
        String vcode = "";
        for (int i = 0; i < captchaLength; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }


}
