package com.tuotiansudai.dto;

public class LoanCreateRequestDto {

    private LoanCreateBaseRequestDto loan;

    private LoanCreateDetailsRequestDto loanDetails;

    private LoanCreateLoanerDetailsRequestDto loanerDetails;

    private LoanCreateLoanerEnterpriseDetailsDto loanerEnterpriseDetails;

    private LoanCreatePledgeHouseRequestDto pledgeHouse;

    private LoanCreatePledgeVehicleRequestDto pledgeVehicle;

    private LoanCreatePledgeEnterpriseRequestDto pledgeEnterprise;

    private String loanMessageTitle;

    private String loanMessageContent;

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

    public String getLoanMessageTitle() {
        return loanMessageTitle;
    }

    public void setLoanMessageTitle(String loanMessageTitle) {
        this.loanMessageTitle = loanMessageTitle;
    }

    public String getLoanMessageContent() {
        return loanMessageContent;
    }

    public void setLoanMessageContent(String loanMessageContent) {
        this.loanMessageContent = loanMessageContent;
    }
}
