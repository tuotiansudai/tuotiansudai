package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsCaptchaMapper {

    void create(SmsCaptchaModel smsCaptchaModel);

    SmsCaptchaModel findByMobile(String mobile);

    void update(SmsCaptchaModel smsCaptchaModel);

    SmsCaptchaModel findByMobileAndCaptchaType(@Param(value = "mobile") String mobile,
                                               @Param(value = "smsCaptchaType") SmsCaptchaType smsCaptchaType);
}
