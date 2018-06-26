package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class WithdrawDownloadDto implements DownloadFilesMatch {

    private static final Map<Integer, String> MATCH = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "merchantNo")
            .put(1, "userName")
            .put(2, "accountNo")
            .put(3, "bankCardNo")
            .put(4, "bankName")
            .put(5, "bankCode")
            .put(6, "status")
            .put(7, "type")
            .put(8, "verifyType")
            .put(9, "feeOwner")
            .put(10, "fee")
            .put(11, "amount")
            .put(12, "receivedAmount")
            .put(13, "orderNo")
            .put(14, "orderDate")
            .put(15, "remark")
            .put(16, "createTime")
            .build());

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

    @Override
    public Map<Integer, String> match() {
        return MATCH;
    }
}
