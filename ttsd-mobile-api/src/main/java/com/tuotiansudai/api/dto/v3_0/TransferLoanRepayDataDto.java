package com.tuotiansudai.api.dto.v3_0;

public class TransferLoanRepayDataDto {
    private String repayDate;
    private String repayAmount;
    private String RepayStatus;

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getRepayStatus() {
        return RepayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        RepayStatus = repayStatus;
    }
}
