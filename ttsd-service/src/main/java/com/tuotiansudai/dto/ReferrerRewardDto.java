package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ReferrerRewardDto {
    @NotEmpty
    private String referrerLoginName;
    @NotEmpty
    private String particUserId;
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String bonus;

    private long orderId;

    public ReferrerRewardDto(){

    }
    public ReferrerRewardDto(String particUserId,String bonus,String referrerLoginName,long orderId){
        this.particUserId = particUserId;
        this.bonus = bonus;
        this.referrerLoginName = referrerLoginName;
        this.orderId = orderId;
    }

    public String getParticUserId() {
        return particUserId;
    }

    public void setParticUserId(String particUserId) {
        this.particUserId = particUserId;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }
}
