package com.tuotiansudai.fudian.dto.response;

public class RechargeContentDto extends PayBaseContentDto {

    private String amount;

    private String fee;

    private String payType;

    private String status; //充值状态  0：充值处理中 1充值成功 2充值失败 3状态不明

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return "1".equalsIgnoreCase(this.status);
    }

}
