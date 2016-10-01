package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreatePledgeEnterpriseRequestDto {
    @NotEmpty
    private String pledgeLocation;
    @NotEmpty
    private String estimateAmount;
    @NotEmpty
    private String guarantee;

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

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }
}
