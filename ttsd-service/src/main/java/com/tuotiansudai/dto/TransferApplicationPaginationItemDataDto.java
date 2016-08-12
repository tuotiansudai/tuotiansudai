package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
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
    private String transferrerMobile;
    private String leftPeriod;
    private String transfereeMobile;
    private String transferFee;
    private Date deadLine;
    private String name;
    private String sumRate;
    private double baseRate;
    private double activityRate;
    private Date applicationTime;
    private Source source;

    public TransferApplicationPaginationItemDataDto(){}

    public TransferApplicationPaginationItemDataDto(TransferApplicationRecordDto transferApplicationRecordDto){
        this.transferApplicationId = String.valueOf(transferApplicationRecordDto.getTransferApplicationId());
        this.loanId = transferApplicationRecordDto.getLoanId();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferAmount());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getInvestAmount());
        this.transferrerMobile = transferApplicationRecordDto.getTransferrerMobile();
        this.transfereeMobile = transferApplicationRecordDto.getTransfereeMobile();
        this.transferFee = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferFee());
        this.transferTime = transferApplicationRecordDto.getTransferTime();
        this.deadLine = transferApplicationRecordDto.getDeadLine();
        this.leftPeriod = String.valueOf(transferApplicationRecordDto.getLeftPeriod());
        this.name = transferApplicationRecordDto.getName();
        this.sumRate = transferApplicationRecordDto.getSumRatePercent();
        this.baseRate = transferApplicationRecordDto.getBaseRate()*100;
        this.activityRate = transferApplicationRecordDto.getActivityRate()*100;
        this.transferStatus = transferApplicationRecordDto.getTransferStatus().name();
        this.applicationTime = transferApplicationRecordDto.getApplicationTime();
        this.source = transferApplicationRecordDto.getSource();
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
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

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getTransferrerMobile() {
        return transferrerMobile;
    }

    public void setTransferrerMobile(String transferrerMobile) {
        this.transferrerMobile = transferrerMobile;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getTransfereeMobile() {
        return transfereeMobile;
    }

    public void setTransfereeMobile(String transfereeMobile) {
        this.transfereeMobile = transfereeMobile;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
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
}
