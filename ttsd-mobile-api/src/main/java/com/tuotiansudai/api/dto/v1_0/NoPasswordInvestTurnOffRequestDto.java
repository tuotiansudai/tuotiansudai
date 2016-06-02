package com.tuotiansudai.api.dto.v1_0;


public class NoPasswordInvestTurnOffRequestDto extends BaseParamDto{
    private String captcha;

    public String getCaptcha() { return captcha; }

    public void setCaptcha(String captcha) { this.captcha = captcha; }

}
