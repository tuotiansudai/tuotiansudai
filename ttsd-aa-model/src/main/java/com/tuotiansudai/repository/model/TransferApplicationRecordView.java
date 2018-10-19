package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class TransferApplicationRecordView implements Serializable {
    private long transferApplicationId;
    private String name;
    private long transferAmount;
    private long investId;
    private long investAmount;
    private Date transferTime;
    private double baseRate;
    private double activityRate;
    private String remainingInterestDays;
    private TransferStatus transferStatus;
    private Long loanId;
    private String transferrerMobile;
    private String transfereeMobile;
    private long transferFee;
    private Long transferInvestId;
    private int period;
    private int leftPeriod;
    private String leftDays;
    private Date deadLine;
    private Date applicationTime;
    private Source source;
    private Date transferInvestTime;

    public String getSumRatePercent() {
        return new DecimalFormat("######0.##").format((baseRate + activityRate) * 100);
    }

    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public String getRemainingInterestDays() {
        return remainingInterestDays;
    }

    public void setRemainingInterestDays(String remainingInterestDays) {
        this.remainingInterestDays = remainingInterestDays;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getTransferrerMobile() {
        return transferrerMobile;
    }

    public void setTransferrerMobile(String transferrerMobile) {
        this.transferrerMobile = transferrerMobile;
    }

    public String getTransfereeMobile() {
        return transfereeMobile;
    }

    public void setTransfereeMobile(String transfereeMobile) {
        this.transfereeMobile = transfereeMobile;
    }

    public long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(long transferFee) {
        this.transferFee = transferFee;
    }

    public Long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(Long transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(int leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Date getTransferInvestTime() {
        return transferInvestTime;
    }

    public void setTransferInvestTime(Date transferInvestTime) {
        this.transferInvestTime = transferInvestTime;
    }
}
