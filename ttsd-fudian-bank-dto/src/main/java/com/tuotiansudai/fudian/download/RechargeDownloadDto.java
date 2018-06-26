package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class RechargeDownloadDto {

    private String merchantNo;
    private String userName;
    private String accountNo;
    private String status;
    private String orderDate;
    private String orderNo;
    private String payType;
    private String feeOwner;
    private String fee;
    private String amount;
    private String receivedAmount;
    private String remark;
    private String createTime;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public static Map<Integer, String> match(){
        return Maps.newHashMap(ImmutableMap.<Integer, String>builder()
                .put(0, "merchantNo")
                .put(1, "userName")
                .put(2, "accountNo")
                .put(3, "status")
                .put(4, "orderDate")
                .put(5, "orderNo")
                .put(6, "payType")
                .put(7, "feeOwner")
                .put(8, "fee")
                .put(9, "amount")
                .put(10, "receivedAmount")
                .put(11, "remark")
                .put(12, "createTime")
                .build());
    }

}
