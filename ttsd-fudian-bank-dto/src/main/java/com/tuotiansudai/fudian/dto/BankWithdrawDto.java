package com.tuotiansudai.fudian.dto;

import com.google.gson.GsonBuilder;

public class BankWithdrawDto extends BankBaseDto {

    private long withdrawId;

    private long amount;

    private boolean isFudianBank;

    public BankWithdrawDto() {
    }

    public BankWithdrawDto(long withdrawId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, boolean isFudianBank, String openId) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.withdrawId = withdrawId;
        this.amount = amount;
        this.isFudianBank = isFudianBank;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean getIsFudianBank() {
        return isFudianBank;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && withdrawId > 0
                && amount > 150;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
