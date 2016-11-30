package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class MembershipPerceptionResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "提示", example = "tip")
    private String tip;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
