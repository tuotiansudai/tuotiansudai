package com.tuotiansudai.api.dto.v1_0;

public class UserCouponRequestDto extends BaseParamDto {

    private boolean used;

    private boolean unused;

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
