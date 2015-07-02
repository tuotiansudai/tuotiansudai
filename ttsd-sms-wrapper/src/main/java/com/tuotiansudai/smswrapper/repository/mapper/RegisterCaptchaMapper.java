package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.RegisterCaptchaModel;

import java.util.List;

public interface RegisterCaptchaMapper {

    void create(RegisterCaptchaModel model);

    List<RegisterCaptchaModel> findByMobile(String mobile);
}
