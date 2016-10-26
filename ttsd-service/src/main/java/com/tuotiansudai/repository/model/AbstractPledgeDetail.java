package com.tuotiansudai.repository.model;

import java.io.Serializable;


public abstract class AbstractPledgeDetail implements Serializable {
    protected long id;
    protected long loanId;
    protected String pledgeLocation;
    protected String estimateAmount;

    public AbstractPledgeDetail() {
    }

    public AbstractPledgeDetail(long loanId, String pledgeLocation, String estimateAmount) {
        this.loanId = loanId;
        this.pledgeLocation = pledgeLocation;
        this.estimateAmount = estimateAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
