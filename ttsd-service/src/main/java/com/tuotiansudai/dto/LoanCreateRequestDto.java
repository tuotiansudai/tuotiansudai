package com.tuotiansudai.dto;

public class LoanCreateRequestDto {

    private LoanCreateBaseRequestDto loan;

    private LoanCreateDetailsRequestDto loanDetails;

    private LoanCreateLoanerDetailsRequestDto loanerDetails;

    private LoanCreateLoanerEnterpriseDetailsDto loanerEnterpriseDetails;

    private LoanCreatePledgeHouseRequestDto pledgeHouse;

    private LoanCreatePledgeVehicleRequestDto pledgeVehicle;

    private LoanCreatePledgeEnterpriseRequestDto pledgeEnterprise;

    private LoanMessageRequestDto loanMessage;

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

    public LoanCreatePledgeHouseRequestDto getPledgeHouse() {
        return pledgeHouse;
    }

    public void setPledgeHouse(LoanCreatePledgeHouseRequestDto pledgeHouse) {
        this.pledgeHouse = pledgeHouse;
    }

    public LoanCreatePledgeVehicleRequestDto getPledgeVehicle() {
        return pledgeVehicle;
    }

    public void setPledgeVehicle(LoanCreatePledgeVehicleRequestDto pledgeVehicle) {
        this.pledgeVehicle = pledgeVehicle;
    }

    public LoanCreatePledgeEnterpriseRequestDto getPledgeEnterprise() {
        return pledgeEnterprise;
    }

    public void setPledgeEnterprise(LoanCreatePledgeEnterpriseRequestDto pledgeEnterprise) {
        this.pledgeEnterprise = pledgeEnterprise;
    }

    public LoanMessageRequestDto getLoanMessage() {
        return loanMessage;
    }

    public void setLoanMessage(LoanMessageRequestDto loanMessage) {
        this.loanMessage = loanMessage;
    }
}
