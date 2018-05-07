package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class WithdrawRequestDto extends PayBaseRequestDto {

    private String amount;

    private String fee = "0.00";

    public WithdrawRequestDto(String userName, String accountNo, String amount, String loginName, String mobile) {
        super(userName, accountNo, ApiType.WITHDRAW, loginName, mobile);
        this.amount = amount;
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