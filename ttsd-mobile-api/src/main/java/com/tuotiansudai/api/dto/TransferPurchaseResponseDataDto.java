package com.tuotiansudai.api.dto;

import java.util.List;

public class TransferPurchaseResponseDataDto extends BaseResponseDataDto {

    private String balance;
    private String transferAmount;
    private String expectedInterestAmount;
    private String transferInterestAmount;

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

    public String getTransferInterestAmount() {
        return transferInterestAmount;
    }

    public void setTransferInterestAmount(String transferInterestAmount) {
        this.transferInterestAmount = transferInterestAmount;
    }




}
