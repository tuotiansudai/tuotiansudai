package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AnxinSignVerifyCaptchaRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "验证码", example = "12345")
    String captcha;

    @ApiModelProperty(value = "是否免密", example = "true：是 ，false：不是")
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