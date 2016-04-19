package com.tuotiansudai.transfer.repository.model;


import com.tuotiansudai.repository.model.TransferStatus;

import java.io.Serializable;
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
}
