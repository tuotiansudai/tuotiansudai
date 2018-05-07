package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class MerchantTransferRequestDto extends PayBaseRequestDto {

    private String amount;

    public MerchantTransferRequestDto(String userName, String accountNo, String amount, String loginName, String mobile) {
        super(userName, accountNo, ApiType.MERCHANT_TRANSFER, loginName, mobile);
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}