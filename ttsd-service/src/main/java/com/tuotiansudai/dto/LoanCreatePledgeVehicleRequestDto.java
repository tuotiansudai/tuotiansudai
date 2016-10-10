package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreatePledgeVehicleRequestDto {
    @NotEmpty
    private String pledgeLocation;
    @NotEmpty
    private String estimateAmount;
    @NotEmpty
    private String pledgeLoanAmount;
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;

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

    public String getPledgeLoanAmount() {
        return pledgeLoanAmount;
    }

    public void setPledgeLoanAmount(String pledgeLoanAmount) {
        this.pledgeLoanAmount = pledgeLoanAmount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
