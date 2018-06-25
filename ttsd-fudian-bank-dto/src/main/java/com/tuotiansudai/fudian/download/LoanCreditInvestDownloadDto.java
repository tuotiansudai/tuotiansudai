package com.tuotiansudai.fudian.download;


import java.io.Serializable;

public class LoanCreditInvestDownloadDto implements Serializable{

    private String merchantNo;
    private String loanTxNo;
    private String buyUserName;
    private String buyAccountNo;
    private String creditAmount;
    private String amount;
    private String creditFeeType;
    private String creditFee;
    private String creditNo;
    private String creditNoAmount;
    private String repayedAmount;
    private String orderNo;
    private String orderDate;
    private String investOrderNo;
    private String investOrderDate;
    private String oriOrderNo;
    private String oriOrderDate;
    private String status;
    private String createTime;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getBuyUserName() {
        return buyUserName;
    }

    public void setBuyUserName(String buyUserName) {
        this.buyUserName = buyUserName;
    }

    public String getBuyAccountNo() {
        return buyAccountNo;
    }

    public void setBuyAccountNo(String buyAccountNo) {
        this.buyAccountNo = buyAccountNo;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreditFeeType() {
        return creditFeeType;
    }

    public void setCreditFeeType(String creditFeeType) {
        this.creditFeeType = creditFeeType;
    }

    public String getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(String creditFee) {
        this.creditFee = creditFee;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getCreditNoAmount() {
        return creditNoAmount;
    }

    public void setCreditNoAmount(String creditNoAmount) {
        this.creditNoAmount = creditNoAmount;
    }

    public String getRepayedAmount() {
        return repayedAmount;
    }

    public void setRepayedAmount(String repayedAmount) {
        this.repayedAmount = repayedAmount;
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

    public String getInvestOrderNo() {
        return investOrderNo;
    }

    public void setInvestOrderNo(String investOrderNo) {
        this.investOrderNo = investOrderNo;
    }

    public String getInvestOrderDate() {
        return investOrderDate;
    }

    public void setInvestOrderDate(String investOrderDate) {
        this.investOrderDate = investOrderDate;
    }

    public String getOriOrderNo() {
        return oriOrderNo;
    }

    public void setOriOrderNo(String oriOrderNo) {
        this.oriOrderNo = oriOrderNo;
    }

    public String getOriOrderDate() {
        return oriOrderDate;
    }

    public void setOriOrderDate(String oriOrderDate) {
        this.oriOrderDate = oriOrderDate;
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
}
