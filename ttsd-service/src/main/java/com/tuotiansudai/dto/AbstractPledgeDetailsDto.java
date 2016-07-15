package com.tuotiansudai.dto;

public abstract class AbstractPledgeDetailsDto {
    private long loanId;
    private String pledgeLocation;
    private String estimateAmount;
    private String loanAmount;

    public AbstractPledgeDetailsDto(long loanId, String pledgeLocation, String estimateAmount, String loanAmount) {
        this.loanId = loanId;
        this.pledgeLocation = pledgeLocation;
        this.estimateAmount = estimateAmount;
        this.loanAmount = loanAmount;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getPledgeLocation() {
        return pledgeLocation;
    }

    public void setPledgeLocation(String pledgeLocation) {
        this.pledgeLocation = pledgeLocation;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }
}
