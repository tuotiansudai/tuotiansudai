package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class SmsCaptchaDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^\\d{6}$")
    private String captcha;

    @NotEmpty
    private String ip;

    private boolean isVoice;

    public SmsCaptchaDto() {
    }

    public SmsCaptchaDto(String mobile, String captcha, boolean isVoice, String ip) {
        this.mobile = mobile;
        this.captcha = captcha;
        this.isVoice = isVoice;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }
}
