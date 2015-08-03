package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SmsCaptchaModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsCaptchaMapper {

    void create(SmsCaptchaModel smsCaptchaModel);

    SmsCaptchaModel findByMobile(String mobile);

    void update(SmsCaptchaModel smsCaptchaModel);
}
