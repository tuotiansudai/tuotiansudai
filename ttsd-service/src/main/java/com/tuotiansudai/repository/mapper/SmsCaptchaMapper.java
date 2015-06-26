package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SmsCaptchaModel;

import java.util.List;

public interface SmsCaptchaMapper {

    public void insertSmsCaptcha(SmsCaptchaModel smsCaptchaModel);

    public List<SmsCaptchaModel> findCaptchabyMobile(SmsCaptchaModel smsCaptchaModel);

    public void updateStatusByMobile(SmsCaptchaModel smsCaptchaModel);

    public SmsCaptchaModel findSmsCaptchaByMobileAndCaptcha(SmsCaptchaModel smsCaptchaModel);
}
