package com.tuotiansudai.dto;

public class CreateVehicleLoanDto extends AbstractCreateLoanDto {
    private String brand;
    private String model;

    public CreateVehicleLoanDto() {
    }

    public AbstractPledgeDetailsDto getPledgeDetailsDto() {
        return new PledgeVehicleDto(id, pledgeLocation, estimateAmount, pledgeLoanAmount, brand, model);
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
