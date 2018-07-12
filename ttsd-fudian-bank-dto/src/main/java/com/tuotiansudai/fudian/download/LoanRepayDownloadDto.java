package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class LoanRepayDownloadDto implements DownloadFilesMatch {

    private static final Map<Integer, String> MATCH = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "merchantNo")
            .put(1, "merchantName")
            .put(2, "loanTxNo")
            .put(3, "orderNo")
            .put(4, "orderDate")
            .put(5, "userName")
            .put(6, "accountNo")
            .put(7, "capital")
            .put(8, "interest")
            .put(9, "loanFee")
            .put(10, "status")
            .put(11, "vouchFlag")
            .put(12, "createTime")
            .build());

    private String merchantNo;
    private String merchantName;
    private String loanTxNo;
    private String orderNo;
    private String orderDate;
    private String userName;
    private String accountNo;
    private String capital;
    private String interest;
    private String loanFee;
    private String status;
    private String vouchFlag;
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

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVouchFlag() {
        return vouchFlag;
    }

    public void setVouchFlag(String vouchFlag) {
        this.vouchFlag = vouchFlag;
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
