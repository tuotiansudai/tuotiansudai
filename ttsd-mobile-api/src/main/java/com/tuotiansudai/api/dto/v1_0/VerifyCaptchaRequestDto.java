package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class VerifyCaptchaRequestDto extends SendSmsCompositeRequestDto{
    @NotEmpty(message = "0020")
    @Pattern(regexp = "^[0-9]{6}$", message = "0009")
    @ApiModelProperty(value = "验证码", example = "123456")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
