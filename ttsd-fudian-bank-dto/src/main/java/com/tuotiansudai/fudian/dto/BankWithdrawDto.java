package com.tuotiansudai.fudian.dto;

import com.google.gson.GsonBuilder;

public class BankWithdrawDto extends BankBaseDto {

    private long withdrawId;

    private long amount;

    private long fee;

    private String openId;

    public BankWithdrawDto() {
    }

    public BankWithdrawDto(long withdrawId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String openId) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.withdrawId = withdrawId;
        this.amount = amount;
        this.fee = fee;
        this.openId = openId;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public long getAmount() {
        return amount;
    }

    public long getFee() {
        return fee;
    }

    public String getOpenId() {
        return openId;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && amount > fee
                && fee > 0;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
