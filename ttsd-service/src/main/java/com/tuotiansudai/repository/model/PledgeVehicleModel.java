package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.PledgeVehicleDto;

public class PledgeVehicleModel extends AbstractPledgeDetail {
    private String brand;
    private String model;

    public PledgeVehicleModel() {
        super();
    }

    public PledgeVehicleModel(long loanId, String pledgeLocation, String estimateAmount, String loanAmount, String brand, String model) {
        super(loanId, pledgeLocation, estimateAmount, loanAmount);
        this.brand = brand;
        this.model = model;
    }

    public PledgeVehicleModel(PledgeVehicleDto pledgeVehicleDto) {
        this.loanId = pledgeVehicleDto.getLoanId();
        this.pledgeLocation = pledgeVehicleDto.getPledgeLocation();
        this.estimateAmount = pledgeVehicleDto.getEstimateAmount();
        this.loanAmount = pledgeVehicleDto.getLoanAmount();
        this.brand = pledgeVehicleDto.getBrand();
        this.model = pledgeVehicleDto.getModel();
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
