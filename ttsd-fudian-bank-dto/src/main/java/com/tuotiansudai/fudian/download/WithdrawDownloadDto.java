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

    public static Map<Integer, String> getMATCH() {
        return MATCH;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getFeeOwner() {
        return feeOwner;
    }

    public void setFeeOwner(String feeOwner) {
        this.feeOwner = feeOwner;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public Map<Integer, String> match() {
        return MATCH;
    }
}
