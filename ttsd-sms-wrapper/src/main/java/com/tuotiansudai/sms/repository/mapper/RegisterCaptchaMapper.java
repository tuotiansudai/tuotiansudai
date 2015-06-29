package com.tuotiansudai.sms.repository.mapper;

import com.tuotiansudai.sms.repository.model.RegisterCaptchaModel;

import java.util.List;

public interface RegisterCaptchaMapper {

    void create(RegisterCaptchaModel model);

    List<RegisterCaptchaModel> findByMobile(String mobile);
}
