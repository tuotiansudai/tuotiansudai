package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;

public class UmpExtraRateRepayDto extends UmpBaseDto{

    private long investExtraRateModelId;

    private long amount;

    private String payAccountId;

    public UmpExtraRateRepayDto() {
    }

    public UmpExtraRateRepayDto(String loginName, String payUserId, long investExtraRateModelId, long amount, String payAccountId) {
        super(loginName, payUserId);
        this.investExtraRateModelId = investExtraRateModelId;
        this.amount = amount;
        this.payAccountId = payAccountId;
    }

    public long getInvestExtraRateModelId() {
        return investExtraRateModelId;
    }

    public long getAmount() {
        return amount;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && investExtraRateModelId > 0
                && amount > 0
                && !Strings.isNullOrEmpty(payAccountId);
    }

}
