package com.tuotiansudai.fudian.download;


import java.io.Serializable;

public class LoanRepayInvestDownloadDto implements Serializable{

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
}
