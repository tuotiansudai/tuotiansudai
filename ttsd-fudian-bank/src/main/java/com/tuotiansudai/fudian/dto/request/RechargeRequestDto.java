package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class RechargeRequestDto extends PayBaseRequestDto {

    private String amount;

    private String fee = "0.00";

    private String payType;

    public RechargeRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, String amount, RechargePayType payType) {
        super(source, loginName, mobile, userName, accountNo, ApiType.RECHARGE, null);
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