package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SmsCaptchaModel;

import java.util.List;

public interface SmsCaptchaMapper {

    void insertSmsCaptcha(SmsCaptchaModel smsCaptchaModel);

    SmsCaptchaModel findRegisterCaptchaByMobile(String mobile);

    void updateSmsCaptchaByMobile(SmsCaptchaModel smsCaptchaModel);

    SmsCaptchaModel findSmsCaptchaByMobileAndCaptcha(SmsCaptchaModel smsCaptchaModel);
}
