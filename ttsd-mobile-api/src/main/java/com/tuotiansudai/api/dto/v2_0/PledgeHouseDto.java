package com.tuotiansudai.api.dto.v2_0;


import java.io.Serializable;

public class PledgeHouseDto implements Serializable {
    private String pledge_location;
    private String square;
    private String estimateAmount;
    private String loanAmount;

    public String getPledge_location() {
        return pledge_location;
    }

    public void setPledge_location(String pledge_location) {
        this.pledge_location = pledge_location;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
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
