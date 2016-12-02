package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class MembershipPurchaseRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "等级", example = "0-5")
    private int level;

    @ApiModelProperty(value = "天数", example = "30")
    private int duration;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
