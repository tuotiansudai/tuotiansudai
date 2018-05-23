package com.tuotiansudai.fudian.dto;


import com.google.gson.GsonBuilder;

public class BankRechargeDto extends BankBaseDto{

    private long rechargeId;

    private long amount;

    public BankRechargeDto() {
    }

    public BankRechargeDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long rechargeId, long amount) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.rechargeId = rechargeId;
        this.amount = amount;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(long rechargeId) {
        this.rechargeId = rechargeId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && rechargeId > 0
                && amount > 0;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
