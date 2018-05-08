package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class WithdrawRequestDto extends PayBaseRequestDto {

    private String amount;

    private String fee = "0.00";

    public WithdrawRequestDto(String loginName, String mobile, String userName, String accountNo, String amount) {
        super(loginName, mobile, userName, accountNo, ApiType.WITHDRAW);
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