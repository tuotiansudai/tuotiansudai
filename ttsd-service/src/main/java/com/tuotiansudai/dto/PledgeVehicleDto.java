package com.tuotiansudai.dto;

public class PledgeVehicleDto extends AbstractPledgeDetailsDto {
    private String brand;
    private String model;

    public PledgeVehicleDto(long loanId, String pledgeLocation, String estimateAmount, String loanAmount, String brand, String model) {
        super(loanId, pledgeLocation, estimateAmount, loanAmount);
        this.brand = brand;
        this.model = model;
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
