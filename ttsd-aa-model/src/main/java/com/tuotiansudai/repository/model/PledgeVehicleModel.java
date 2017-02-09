package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreatePledgeVehicleRequestDto;
import com.tuotiansudai.dto.PledgeVehicleDto;

public class PledgeVehicleModel extends AbstractPledgeDetail {
    private String loanAmount;
    private String brand;
    private String model;

    public PledgeVehicleModel() {
        super();
    }

    public PledgeVehicleModel(long loanId, String pledgeLocation, String estimateAmount, String loanAmount, String brand, String model) {
        super(loanId, pledgeLocation, estimateAmount);
        this.loanAmount = loanAmount;
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

    public PledgeVehicleModel(long loanId, LoanCreatePledgeVehicleRequestDto pledgeVehicleDto) {
        this.loanId = loanId;
        this.pledgeLocation = pledgeVehicleDto.getPledgeLocation();
        this.estimateAmount = pledgeVehicleDto.getEstimateAmount();
        this.loanAmount = pledgeVehicleDto.getPledgeLoanAmount();
        this.brand = pledgeVehicleDto.getBrand();
        this.model = pledgeVehicleDto.getModel();
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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
