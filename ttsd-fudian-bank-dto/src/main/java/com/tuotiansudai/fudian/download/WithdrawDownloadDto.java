package com.tuotiansudai.fudian.download;


import java.io.Serializable;

public class WithdrawDownloadDto implements Serializable{

    private String merchantNo;
    private String userName;
    private String accountNo;
    private String bankCardNo;
    private String bankName;
    private String bankCode;
    private String status;
    private String type;
    private String verifyType;
    private String feeOwner;
    private String fee;
    private String amount;
    private String receivedAmount;
    private String orderNo;
    private String orderDate;
    private String remark;
    private String createTime;

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public void setFeeOwner(String feeOwner) {
        this.feeOwner = feeOwner;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
