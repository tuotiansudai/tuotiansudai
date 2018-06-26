package com.tuotiansudai.fudian.download;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class LoanRepayInvestDownloadDto implements DownloadFilesMatch {

    private static final Map<Integer, String> MATCH = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "merchantNo")
            .put(1, "merchantName")
            .put(2, "loanTxNo")
            .put(3, "investOrederNo")
            .put(4, "investOrderDate")
            .put(5, "investUserName")
            .put(6, "investAccountNo")
            .put(7, "capital")
            .put(8, "interest")
            .put(9, "interestFee")
            .put(10, "rateInterest")
            .put(11, "orderNo")
            .put(12, "orderDate")
            .put(13, "createTime")
            .build());

    private String merchantNo;
    private String merchantName;
    private String loanTxNo;
    private String investOrederNo;
    private String investOrderDate;
    private String investUserName;
    private String investAccountNo;
    private String capital;
    private String interest;
    private String interestFee;
    private String rateInterest;
    private String orderNo;
    private String orderDate;
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

    public String getInvestOrederNo() {
        return investOrederNo;
    }

    public void setInvestOrederNo(String investOrederNo) {
        this.investOrederNo = investOrederNo;
    }

    public String getInvestOrderDate() {
        return investOrderDate;
    }

    public void setInvestOrderDate(String investOrderDate) {
        this.investOrderDate = investOrderDate;
    }

    public String getInvestUserName() {
        return investUserName;
    }

    public void setInvestUserName(String investUserName) {
        this.investUserName = investUserName;
    }

    public String getInvestAccountNo() {
        return investAccountNo;
    }

    public void setInvestAccountNo(String investAccountNo) {
        this.investAccountNo = investAccountNo;
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

    public String getInterestFee() {
        return interestFee;
    }

    public void setInterestFee(String interestFee) {
        this.interestFee = interestFee;
    }

    public String getRateInterest() {
        return rateInterest;
    }

    public void setRateInterest(String rateInterest) {
        this.rateInterest = rateInterest;
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
