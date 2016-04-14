package com.tuotiansudai.api.dto;

public class TransferPurchaseResponseDataDto extends BaseResponseDataDto {

    private String balance;
    private String transferAmount;
    private String expectedInterestAmount;

    public TransferPurchaseResponseDataDto(){}

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getExpectedInterestAmount() {
        return expectedInterestAmount;
    }

    public void setExpectedInterestAmount(String expectedInterestAmount) {
        this.expectedInterestAmount = expectedInterestAmount;
    }
}
