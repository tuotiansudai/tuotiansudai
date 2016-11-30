package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferPurchaseResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "账户余额", example = "100")
    private String balance;

    @ApiModelProperty(value = "认购金额", example = "100")
    private String transferAmount;

    @ApiModelProperty(value = "预计利息A", example = "10")
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
