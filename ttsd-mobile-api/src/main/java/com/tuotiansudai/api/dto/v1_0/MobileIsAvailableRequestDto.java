package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class MobileIsAvailableRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "手机号", example = "15900000001")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
