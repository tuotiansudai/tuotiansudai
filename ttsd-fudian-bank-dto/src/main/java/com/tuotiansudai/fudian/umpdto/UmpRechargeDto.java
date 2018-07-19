package com.tuotiansudai.fudian.umpdto;


public class UmpRechargeDto extends UmpBaseDto{

    private long rechargeId;

    private long amount;

    private boolean fastPay;

    private String bankCode;

    public UmpRechargeDto() {
    }

    public UmpRechargeDto(String loginName, String payUserId, long rechargeId, long amount, boolean fastPay, String bankCode) {
        super(loginName, payUserId);
        this.rechargeId = rechargeId;
        this.amount = amount;
        this.fastPay = fastPay;
        this.bankCode = bankCode;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public String getBankCode() {
        return bankCode;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && rechargeId > 0
                && amount > 0;
    }
}
