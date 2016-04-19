package com.tuotiansudai.api.dto;


import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.text.DecimalFormat;

public class TransferApplicationRecordResponseDataDto extends BaseResponseDataDto{
    private String transferApplicationId;
    private String name;
    private String transferAmount;
    private String investAmount;
    private String transferTime;
    private String baseRate;
    private String activityRate;
    private String remainingInterestDays;
    private TransferStatus transferStatus;

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) { this.transferApplicationId = transferApplicationId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public String getRemainingInterestDays() { return remainingInterestDays; }

    public void setRemainingInterestDays(String remainingInterestDays) {this.remainingInterestDays = remainingInterestDays; }

    public TransferStatus getTransferStatus() { return transferStatus; }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public TransferApplicationRecordResponseDataDto(){

    }
    public TransferApplicationRecordResponseDataDto(TransferApplicationRecordDto transferApplicationRecordDto){
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.transferApplicationId = String.valueOf(transferApplicationRecordDto.getTransferApplicationId());
        this.name = transferApplicationRecordDto.getName();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferAmount());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getInvestAmount());
        this.transferTime = transferApplicationRecordDto.getTransferTime() == null?"":new DateTime(transferApplicationRecordDto.getTransferTime()).toString("yyyy-MM-dd HH:mm:ss");
        this.baseRate = decimalFormat.format(transferApplicationRecordDto.getBaseRate() * 100);
        this.activityRate = decimalFormat.format(transferApplicationRecordDto.getActivityRate() * 100);
        this.transferStatus = transferApplicationRecordDto.getTransferStatus();
        this.remainingInterestDays = String.valueOf(transferApplicationRecordDto.getRemainingInterestDays());
    }
}
