package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanOutTailAfterModel;

import java.util.List;

public class LoanCreateRequestDto {

    private LoanCreateBaseRequestDto loan;

    private LoanCreateDetailsRequestDto loanDetails;

    private LoanCreateLoanerDetailsRequestDto loanerDetails;

    private LoanCreateLoanerEnterpriseDetailsDto loanerEnterpriseDetails;

    private List<LoanCreatePledgeHouseRequestDto> pledgeHouse;

    private List<LoanCreatePledgeVehicleRequestDto> pledgeVehicle;

    private List<LoanCreatePledgeEnterpriseRequestDto> pledgeEnterprise;

    private LoanCreateLoanerEnterpriseInfoDto loanerEnterpriseInfo;

    private String loanApplicationId;

    public LoanCreateBaseRequestDto getLoan() {
        return loan;
    }

    public void setLoan(LoanCreateBaseRequestDto loan) {
        this.loan = loan;
    }

    public LoanCreateDetailsRequestDto getLoanDetails() {
        return loanDetails;
    }

    public void setLoanDetails(LoanCreateDetailsRequestDto loanDetails) {
        this.loanDetails = loanDetails;
    }

    public LoanCreateLoanerDetailsRequestDto getLoanerDetails() {
        return loanerDetails;
    }

    public void setLoanerDetails(LoanCreateLoanerDetailsRequestDto loanerDetails) {
        this.loanerDetails = loanerDetails;
    }

    public LoanCreateLoanerEnterpriseDetailsDto getLoanerEnterpriseDetails() {
        return loanerEnterpriseDetails;
    }

    public void setLoanerEnterpriseDetails(LoanCreateLoanerEnterpriseDetailsDto loanerEnterpriseDetails) {
        this.loanerEnterpriseDetails = loanerEnterpriseDetails;
    }

    public List<LoanCreatePledgeHouseRequestDto> getPledgeHouse() {
        return pledgeHouse;
    }

    public void setPledgeHouse(List<LoanCreatePledgeHouseRequestDto> pledgeHouse) {
        this.pledgeHouse = pledgeHouse;
    }

    public List<LoanCreatePledgeVehicleRequestDto> getPledgeVehicle() {
        return pledgeVehicle;
    }

    public void setPledgeVehicle(List<LoanCreatePledgeVehicleRequestDto> pledgeVehicle) {
        this.pledgeVehicle = pledgeVehicle;
    }

    public List<LoanCreatePledgeEnterpriseRequestDto> getPledgeEnterprise() {
        return pledgeEnterprise;
    }

    public void setPledgeEnterprise(List<LoanCreatePledgeEnterpriseRequestDto> pledgeEnterprise) {
        this.pledgeEnterprise = pledgeEnterprise;
    }

    public LoanCreateLoanerEnterpriseInfoDto getLoanerEnterpriseInfo() {
        return loanerEnterpriseInfo;
    }

    public void setLoanerEnterpriseInfo(LoanCreateLoanerEnterpriseInfoDto loanerEnterpriseInfo) {
        this.loanerEnterpriseInfo = loanerEnterpriseInfo;
    }

    public String getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(String loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }
}
