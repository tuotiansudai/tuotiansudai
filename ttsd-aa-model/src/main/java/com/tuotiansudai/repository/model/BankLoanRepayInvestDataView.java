package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class BankLoanRepayInvestDataView implements Serializable {

    private long investId;
    private Long transferInvestId;
    private long investAmount;
    private long investRepayId;
    private String loanTxNo;
    private String loginName;
    private String mobile;
    private String bankUserName;
    private String bankAccountNo;
    private long corpus;
    private long expectedInterest;
    private long defaultInterest;
    private long expectedFee;
    private double investFeeRate;
    private Date tradingTime;
    private String investBankOrderNo;
    private String investBankOrderDate;

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public Long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(Long transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public void setInvestRepayId(long investRepayId) {
        this.investRepayId = investRepayId;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public long getCorpus() {
        return corpus;
    }

    public void setCorpus(long corpus) {
        this.corpus = corpus;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(long defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(long expectedFee) {
        this.expectedFee = expectedFee;
    }

    public double getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(double investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public Date getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(Date tradingTime) {
        this.tradingTime = tradingTime;
    }

    public String getInvestBankOrderNo() {
        return investBankOrderNo;
    }

    public void setInvestBankOrderNo(String investBankOrderNo) {
        this.investBankOrderNo = investBankOrderNo;
    }

    public String getInvestBankOrderDate() {
        return investBankOrderDate;
    }

    public void setInvestBankOrderDate(String investBankOrderDate) {
        this.investBankOrderDate = investBankOrderDate;
    }
}
