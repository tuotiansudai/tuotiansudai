package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class UserCouponRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "使用状态", example = "未使用")
    private boolean used;

    @ApiModelProperty(value = "使用状态", example = "已使用")
    private boolean unused;

    @ApiModelProperty(value = "使用状态", example = "已过期")
    private boolean expired;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUnused() {
        return unused;
    }

    public void setUnused(boolean unused) {
        this.unused = unused;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
