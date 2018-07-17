package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;

public class UmpExperienceRepayDto extends UmpBaseDto{

    private long investRepayModelId;

    private long amount;

    private String payAccountId;

    public UmpExperienceRepayDto() {
    }

    public UmpExperienceRepayDto(String loginName, String payUserId, long investRepayModelId, long amount, String payAccountId) {
        super(loginName, payUserId);
        this.investRepayModelId = investRepayModelId;
        this.amount = amount;
        this.payAccountId = payAccountId;
    }

    public long getInvestRepayModelId() {
        return investRepayModelId;
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
                && investRepayModelId > 0
                && amount > 0
                && !Strings.isNullOrEmpty(payAccountId);
    }
}
