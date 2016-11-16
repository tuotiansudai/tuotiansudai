package com.tuotiansudai.api.dto.v1_0;

public class AnxinSignVerifyCaptchaRequestDto extends BaseParamDto {

    String captcha;

    boolean skipAuth;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(boolean skipAuth) {
        this.skipAuth = skipAuth;
    }
}