package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.PledgeVehicleModel;
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

    public LoanCreatePledgeVehicleRequestDto() {
    }

    public LoanCreatePledgeVehicleRequestDto(PledgeVehicleModel pledgeVehicleModel) {
        this.pledgeLocation = pledgeVehicleModel.getPledgeLocation();
        this.estimateAmount = pledgeVehicleModel.getEstimateAmount();
        this.pledgeLoanAmount = pledgeVehicleModel.getLoanAmount();
        this.brand = pledgeVehicleModel.getBrand();
        this.model = pledgeVehicleModel.getModel();
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
