package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class WithdrawRequestDto extends PayBaseRequestDto {

    private String amount;

    private String fee;

    public WithdrawRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, String amount, String fee) {
        super(source, loginName, mobile, userName, accountNo, ApiType.WITHDRAW, null);
        this.amount = amount;
        this.fee = fee;
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
}