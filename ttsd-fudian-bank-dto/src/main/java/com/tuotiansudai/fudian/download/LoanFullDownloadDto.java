package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class LoanFullDownloadDto implements DownloadFilesMatch {

    private static final Map<Integer, String> MATCH = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "merchantNo")
            .put(1, "merchantName")
            .put(2, "loanTxNo")
            .put(3, "loanTxName")
            .put(4, "amount")
            .put(5, "loanFee")
            .put(6, "orderNo")
            .put(7, "orderDate")
            .put(8, "username")
            .put(9, "accountNo")
            .put(10, "status")
            .put(11, "createTime")
            .build());

    private String merchantNo;
    private String merchantName;
    private String loanTxNo;
    private String loanTxName;
    private String amount;
    private String loanFee;
    private String orderNo;
    private String orderDate;
    private String username;
    private String accountNo;
    private String status;
    private String createTime;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoanTxName() {
        return loanTxName;
    }

    public void setLoanTxName(String loanTxName) {
        this.loanTxName = loanTxName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
