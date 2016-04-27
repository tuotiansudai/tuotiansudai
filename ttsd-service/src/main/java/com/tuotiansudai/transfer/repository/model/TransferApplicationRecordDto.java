package com.tuotiansudai.transfer.repository.model;


import com.tuotiansudai.repository.model.TransferStatus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class TransferApplicationRecordDto implements Serializable {
    private long transferApplicationId;
    private String name;
    private long transferAmount;
    private long investAmount;
    private Date transferTime;
    private double baseRate;
    private double activityRate;
    private TransferStatus transferStatus;
    private Long loanId;
    private String transferrerLoginName;
    private String transfereeLoginName;
    private long transferFee;
    private Long transferInvestId;
    private int period;
    private int leftPeriod;
    private Date deadLine;

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

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferrerLoginName() {
        return transferrerLoginName;
    }

    public void setTransferrerLoginName(String transferrerLoginName) {
        this.transferrerLoginName = transferrerLoginName;
    }

    public String getTransfereeLoginName() {
        return transfereeLoginName;
    }

    public void setTransfereeLoginName(String transfereeLoginName) {
        this.transfereeLoginName = transfereeLoginName;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getSumRatePercent(){
        return new DecimalFormat("######0.##").format((baseRate + activityRate) * 100);
    }
}
