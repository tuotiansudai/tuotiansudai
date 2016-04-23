package com.tuotiansudai.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class VerifyCaptchaRequestDto extends SendSmsCompositeRequestDto{
    @NotEmpty(message = "0020")
    @Pattern(regexp = "^[0-9]{6}$", message = "0009")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
