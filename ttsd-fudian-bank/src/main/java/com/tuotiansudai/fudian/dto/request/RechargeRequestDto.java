package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class RechargeRequestDto extends PayBaseRequestDto {

    private String amount;

    private String fee = "0.00";

    private String payType;

    public RechargeRequestDto(String userName, String accountNo, String amount, RechargePayType payType) {
        super(userName, accountNo, ApiType.RECHARGE.name());
        this.amount = amount;
        this.payType = payType.getValue();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}