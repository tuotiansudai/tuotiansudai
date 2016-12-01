package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class NoPasswordInvestTurnOnRequestDto extends BaseParamDto {
    @ApiModelProperty(value = "验证码", example = "12345")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
