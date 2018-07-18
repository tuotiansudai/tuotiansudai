package com.tuotiansudai.fudian.umpdto;


public class UmpWithdrawDto extends UmpBaseDto{

    private long withdrawId;

    private long amount;

    public UmpWithdrawDto() {
    }

    public UmpWithdrawDto(String loginName, String payUserId, long withdrawId, long amount) {
        super(loginName, payUserId);
        this.withdrawId = withdrawId;
        this.amount = amount;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && withdrawId > 0
                && amount > 0;
    }
}
