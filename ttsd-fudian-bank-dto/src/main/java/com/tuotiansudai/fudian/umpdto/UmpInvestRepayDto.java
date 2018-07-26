package com.tuotiansudai.fudian.umpdto;


public class UmpInvestRepayDto extends UmpBaseDto {

    private long loanId;

    private long investId;

    private long investRepayId;

    private long corpus;

    private long interest;

    private long fee;

    private long defaultFee;

    private boolean isNormalRepay;

    public UmpInvestRepayDto() {
    }

    public UmpInvestRepayDto(String loginName, String payUserId, long loanId, long investId, long investRepayId, long corpus, long interest, long fee, long defaultFee, boolean isNormalRepay) {
        super(loginName, payUserId);
        this.loanId = loanId;
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.corpus = corpus;
        this.interest = interest;
        this.fee = fee;
        this.defaultFee = defaultFee;
        this.isNormalRepay = isNormalRepay;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getInvestId() {
        return investId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public long getCorpus() {
        return corpus;
    }

    public long getInterest() {
        return interest;
    }

    public long getFee() {
        return fee;
    }

    public long getDefaultFee() {
        return defaultFee;
    }

    public boolean getIsNormalRepay() {
        return isNormalRepay;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && investId > 0
                && investRepayId > 0
                && corpus >= 0
                && interest > 0
                && fee >= 0
                && defaultFee >= 0;
    }
}
