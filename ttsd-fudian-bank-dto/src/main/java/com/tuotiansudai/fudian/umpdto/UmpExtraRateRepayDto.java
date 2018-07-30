package com.tuotiansudai.fudian.umpdto;


public class UmpExtraRateRepayDto extends UmpBaseDto{

    private long investExtraRateId;

    private long interest;

    private long fee;


    public UmpExtraRateRepayDto() {
    }

    public UmpExtraRateRepayDto(String loginName, String payUserId, long investExtraRateId, long interest, long fee) {
        super(loginName, payUserId);
        this.investExtraRateId = investExtraRateId;
        this.interest = interest;
        this.fee = fee;
    }

    public long getInvestExtraRateId() {
        return investExtraRateId;
    }

    public long getInterest() {
        return interest;
    }

    public long getFee() {
        return fee;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && investExtraRateId > 0
                && interest > 0
                && fee >= 0;
    }
}
