package com.tuotiansudai.api.dto.v1_0;

public class MembershipPurchaseRequestDto extends BaseParamDto {

    private int level;

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
