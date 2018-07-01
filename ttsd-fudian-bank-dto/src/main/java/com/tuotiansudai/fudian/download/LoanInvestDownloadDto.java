package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class LoanInvestDownloadDto implements DownloadFilesMatch {

    private static final Map<Integer, String> MATCH = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "merchantNo")
            .put(1, "userName")
            .put(2, "accountNo")
            .put(3, "loanTxNo")
            .put(4, "orderNo")
            .put(5, "orderDate")
            .put(6, "amount")
            .put(7, "award")
            .put(8, "creditAccount")
            .put(9, "repayedCapital")
            .put(10, "repayedInterest")
            .put(11, "type")
            .put(12, "investType")
            .put(13, "status")
            .put(1, "status")
            .put(13, "status")
            .build());


    private String merchantNo;
    private String userName;
    private String accountNo;
    private String loanTxNo;
    private String orderNo;
    private String orderDate;
    private String amount;
    private String award;
    private String creditAccount;
    private String repayedCapital;
    private String repayedInterest;
    private String type;
    private String investType;
    private String status;
    private String rateInterest;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getRepayedCapital() {
        return repayedCapital;
    }

    public void setRepayedCapital(String repayedCapital) {
        this.repayedCapital = repayedCapital;
    }

    public String getRepayedInterest() {
        return repayedInterest;
    }

    public void setRepayedInterest(String repayedInterest) {
        this.repayedInterest = repayedInterest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRateInterest() {
        return rateInterest;
    }

    public void setRateInterest(String rateInterest) {
        this.rateInterest = rateInterest;
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
