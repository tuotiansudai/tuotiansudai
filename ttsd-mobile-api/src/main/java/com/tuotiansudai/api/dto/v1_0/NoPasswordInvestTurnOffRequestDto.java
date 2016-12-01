package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class NoPasswordInvestTurnOffRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "短信验证码", example = "123456")
    private String captcha;

    public String getCaptcha() { return captcha; }

    public void setCaptcha(String captcha) { this.captcha = captcha; }

}
