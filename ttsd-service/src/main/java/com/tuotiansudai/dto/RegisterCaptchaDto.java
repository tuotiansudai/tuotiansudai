package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RegisterCaptchaDto {

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{5}$")
    private String imageCaptcha;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageCaptcha() {
        return imageCaptcha;
    }

    public void setImageCaptcha(String imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }
}
