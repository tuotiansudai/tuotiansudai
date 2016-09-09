package com.tuotiansudai.api.dto.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;

import java.util.List;

public class TransfereeTransferLoanDataDto extends BaseResponseDataDto {
    private String loanId;
    private String loanName;
    private String investAmount;
    private String expectBenefit;
    private String repaidBenefit;
    private String toRepayBenefit;
    private String deadLine;
    private List<TransferLoanRepayDataDto> repayDetails;
    private String expectRate;
    private String feeDiscount;
    private String transferTime;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getExpectBenefit() {
        return expectBenefit;
    }

    public void setExpectBenefit(String expectBenefit) {
        this.expectBenefit = expectBenefit;
    }

    public String getRepaidBenefit() {
        return repaidBenefit;
    }

    public void setRepaidBenefit(String repaidBenefit) {
        this.repaidBenefit = repaidBenefit;
    }

    public String getToRepayBenefit() {
        return toRepayBenefit;
    }

    public void setToRepayBenefit(String toRepayBenefit) {
        this.toRepayBenefit = toRepayBenefit;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public List<TransferLoanRepayDataDto> getRepayDetails() {
        return repayDetails;
    }

    public void setRepayDetails(List<TransferLoanRepayDataDto> repayDetails) {
        this.repayDetails = repayDetails;
    }

    public String getExpectRate() {
        return expectRate;
    }

    public void setExpectRate(String expectRate) {
        this.expectRate = expectRate;
    }

    public String getFeeDiscount() {
        return feeDiscount;
    }

    public void setFeeDiscount(String feeDiscount) {
        this.feeDiscount = feeDiscount;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }
}
