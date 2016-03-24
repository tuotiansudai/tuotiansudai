package com.tuotiansudai.api.dto;


public class NoPasswordInvestTurnOffRequestDto extends BaseParamDto{
    private String captcha;

    public String getCaptcha() { return captcha; }

    public void setCaptcha(String captcha) { this.captcha = captcha; }

}
