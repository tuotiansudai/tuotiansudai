package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class MembershipPrivilegePurchaseRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "天数", example = "30")
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
