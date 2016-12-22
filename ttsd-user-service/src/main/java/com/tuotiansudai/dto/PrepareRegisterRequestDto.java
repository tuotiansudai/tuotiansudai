package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class PrepareRegisterRequestDto implements Serializable {
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String referrerMobile;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$")
    private String captcha;

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
