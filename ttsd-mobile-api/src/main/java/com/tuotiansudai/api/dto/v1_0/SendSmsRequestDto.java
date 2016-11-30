package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class SendSmsRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "验证码类别", example = "register")
    private String type;

    @ApiModelProperty(value = "手机号", example = "15900000001")
    private String phoneNum;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

}
