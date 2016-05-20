package com.tuotiansudai.api.dto;


public class TransferApplyQueryResponseDataDto extends BaseResponseDataDto {

    private String investAmount;
    private String transferFee;
    private String deadLine;
    private String discountUpper;
    private String discountLower;

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }


    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getDiscountUpper() {
        return discountUpper;
    }

    public void setDiscountUpper(String discountUpper) {
        this.discountUpper = discountUpper;
    }

    public String getDiscountLower() {
        return discountLower;
    }

    public void setDiscountLower(String discountLower) {
        this.discountLower = discountLower;
    }
}
