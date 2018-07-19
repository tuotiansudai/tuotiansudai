package com.tuotiansudai.fudian.umpdto;


public class UmpRechargeDto extends UmpBaseDto{

    private long rechargeId;

    private long amount;

    private boolean isFastPay;

    private String bankCode;

    public UmpRechargeDto() {
    }

    public UmpRechargeDto(String loginName, String payUserId, long rechargeId, long amount, boolean isFastPay, String bankCode) {
        super(loginName, payUserId);
        this.rechargeId = rechargeId;
        this.amount = amount;
        this.isFastPay = isFastPay;
        this.bankCode = bankCode;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isFastPay() {
        return isFastPay;
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
