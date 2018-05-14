package com.tuotiansudai.fudian.dto.response;

public class MerchantTransferContentDto extends PayBaseContentDto {

    private String amount;

    private String status; // 1 成功 -1 失败

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
