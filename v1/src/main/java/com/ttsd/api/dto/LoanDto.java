package com.ttsd.api.dto;

public class LoanDto {
    private String loanId;
    private String loanType;
    private String loanName;
    private String repayTypeCode;
    private String repayTypeName;
    private Integer deadline;
    private String repayUnit;
    private Double ratePercent;
    private Double loanMoney;
    private String loanStatus;
    private String loanStatusDesc;
    private Double investedMoney;
    private Double jkRatePercent;
    private Double hdRatePercent;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getRepayTypeCode() {
        return repayTypeCode;
    }

    public void setRepayTypeCode(String repayTypeCode) {
        this.repayTypeCode = repayTypeCode;
    }

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getRepayUnit() {
        return repayUnit;
    }

    public void setRepayUnit(String repayUnit) {
        this.repayUnit = repayUnit;
    }

    public Double getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(Double ratePercent) {
        this.ratePercent = ratePercent;
    }

    public Double getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(Double loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatusDesc() {
        return loanStatusDesc;
    }

    public void setLoanStatusDesc(String loanStatusDesc) {
        this.loanStatusDesc = loanStatusDesc;
    }

    public Double getInvestedMoney() {
        return investedMoney;
    }

    public void setInvestedMoney(Double investedMoney) {
        this.investedMoney = investedMoney;
    }

    public Double getJkRatePercent() {
        return jkRatePercent;
    }

    public void setJkRatePercent(Double jkRatePercent) {
        this.jkRatePercent = jkRatePercent;
    }

    public Double getHdRatePercent() {
        return hdRatePercent;
    }

    public void setHdRatePercent(Double hdRatePercent) {
        this.hdRatePercent = hdRatePercent;
    }
}
