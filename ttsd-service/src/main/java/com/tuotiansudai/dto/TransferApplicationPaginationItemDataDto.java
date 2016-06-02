package com.tuotiansudai.dto;

import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class TransferApplicationPaginationItemDataDto implements Serializable {
    private String transferApplicationId;
    private String transferAmount;
    private String investAmount;
    private Date transferTime;
    private String transferStatus;
    private long loanId;
    private String transferrerLoginName;
    private String leftPeriod;
    private String transfereeLoginName;
    private String transferFee;
    private Date deadLine;
    private String name;
    private String sumRate;
    private double baseRate;
    private double activityRate;
    private Date applicationTime;

    public TransferApplicationPaginationItemDataDto(){}

    public TransferApplicationPaginationItemDataDto(TransferApplicationRecordDto transferApplicationRecordDto){
        this.transferApplicationId = String.valueOf(transferApplicationRecordDto.getTransferApplicationId());
        this.loanId = transferApplicationRecordDto.getLoanId();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferAmount());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getInvestAmount());
        this.transferrerLoginName = transferApplicationRecordDto.getTransferrerLoginName();
        this.transfereeLoginName = transferApplicationRecordDto.getTransfereeLoginName();
        this.transferFee = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferFee());
        this.transferTime = transferApplicationRecordDto.getTransferTime();
        this.deadLine = transferApplicationRecordDto.getDeadLine();
        this.leftPeriod = String.valueOf(transferApplicationRecordDto.getLeftPeriod());
        this.name = transferApplicationRecordDto.getName();
        this.sumRate = transferApplicationRecordDto.getSumRatePercent();
        this.baseRate = transferApplicationRecordDto.getBaseRate()*100;
        this.activityRate = transferApplicationRecordDto.getActivityRate()*100;
        this.setTransferStatus(transferApplicationRecordDto.getTransferStatus().name());
        this.applicationTime = transferApplicationRecordDto.getApplicationTime();
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferrerLoginName() {
        return transferrerLoginName;
    }

    public void setTransferrerLoginName(String transferrerLoginName) {
        this.transferrerLoginName = transferrerLoginName;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getTransfereeLoginName() {
        return transfereeLoginName;
    }

    public void setTransfereeLoginName(String transfereeLoginName) {
        this.transfereeLoginName = transfereeLoginName;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSumRate() {
        return sumRate;
    }

    public void setSumRate(String sumRate) {
        this.sumRate = sumRate;
    }

    public Date getDeadLine() {return deadLine;}

    public void setDeadLine(Date deadLine) {this.deadLine = deadLine;}

    public double getBaseRate() { return baseRate; }

    public void setBaseRate(double baseRate) { this.baseRate = baseRate; }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public double getActivityRate() { return activityRate; }

    public void setActivityRate(double activityRate) { this.activityRate = activityRate; }




}
