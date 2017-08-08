package com.tuotiansudai.dto;

public class RedeemLimitsDataDto extends BaseDataDto {

    private long totalAmount;

    private long remainAmount;

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(long remainAmount) {
        this.remainAmount = remainAmount;
    }
}
