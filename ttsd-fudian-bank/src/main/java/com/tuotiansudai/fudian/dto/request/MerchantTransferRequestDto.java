package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class MerchantTransferRequestDto extends PayBaseRequestDto {

    private String amount;

    public MerchantTransferRequestDto(String loginName, String mobile, String userName, String accountNo, String amount, Map<String, String> extraValues) {
        super(Source.WEB, loginName, mobile, userName, accountNo, ApiType.MERCHANT_TRANSFER, extraValues);
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}