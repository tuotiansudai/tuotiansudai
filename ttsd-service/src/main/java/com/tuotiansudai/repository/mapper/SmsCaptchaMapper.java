package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SmsCaptchaModel;

import java.util.List;

public interface SmsCaptchaMapper {

    public void insertSmsCaptcha(SmsCaptchaModel smsCaptchaModel);

    public SmsCaptchaModel findRegisterCaptchaByMobile(String mobile);

    public void updateSmsCaptchaByMobile(SmsCaptchaModel smsCaptchaModel);

    public SmsCaptchaModel findSmsCaptchaByMobileAndCaptcha(SmsCaptchaModel smsCaptchaModel);
}
