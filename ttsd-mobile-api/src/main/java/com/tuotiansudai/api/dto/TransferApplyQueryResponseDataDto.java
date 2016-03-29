package com.tuotiansudai.api.dto;


public class TransferApplyQueryResponseDataDto extends BaseResponseDataDto {

    private String investAmount;
    private String transferInterestDays;
    private String transferInterest;
    private String transferFee;

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getTransferInterestDays() {
        return transferInterestDays;
    }

    public void setTransferInterestDays(String transferInterestDays) {
        this.transferInterestDays = transferInterestDays;
    }

    public String getTransferInterest() {
        return transferInterest;
    }

    public void setTransferInterest(String transferInterest) {
        this.transferInterest = transferInterest;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }
}
