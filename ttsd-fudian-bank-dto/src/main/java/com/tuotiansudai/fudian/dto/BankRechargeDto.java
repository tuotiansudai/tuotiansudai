package com.tuotiansudai.fudian.dto;


import com.google.gson.GsonBuilder;

public class BankRechargeDto extends BankBaseDto{

    private long rechargeId;

    private long amount;

    private RechargePayType payType;

    public BankRechargeDto() {
    }

    public BankRechargeDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long rechargeId, long amount, RechargePayType payType) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.rechargeId = rechargeId;
        this.amount = amount;
        this.payType = payType;
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

    public RechargePayType getPayType() {
        return payType;
    }

    public void setPayType(RechargePayType payType) {
        this.payType = payType;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && rechargeId > 0
                && amount > 0
                && payType != null;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
