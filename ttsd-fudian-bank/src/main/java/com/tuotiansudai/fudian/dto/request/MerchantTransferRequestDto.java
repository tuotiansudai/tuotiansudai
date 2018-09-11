package com.tuotiansudai.fudian.dto.request;

public class MerchantTransferRequestDto extends NotifyRequestDto {

    private String amount;

    public MerchantTransferRequestDto(String loginName, String mobile, String userName, String accountNo, String amount) {
        super(Source.WEB, loginName, mobile, userName, accountNo);
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}