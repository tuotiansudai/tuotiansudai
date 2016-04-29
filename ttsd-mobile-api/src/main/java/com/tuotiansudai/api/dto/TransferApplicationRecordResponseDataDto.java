package com.tuotiansudai.api.dto;


import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
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
    private TransferStatus transferStatus;
    private String leftPeriod;

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

    public TransferStatus getTransferStatus() { return transferStatus; }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
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
        this.leftPeriod = String.valueOf(transferApplicationRecordDto.getLeftPeriod());
    }

    public TransferApplicationRecordResponseDataDto(TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto){
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.transferApplicationId = String.valueOf(transferApplicationPaginationItemDataDto.getTransferApplicationId());
        this.name = transferApplicationPaginationItemDataDto.getTransferName();
        this.transferAmount = transferApplicationPaginationItemDataDto.getTransferAmount();
        this.investAmount =  transferApplicationPaginationItemDataDto.getInvestAmount();
        this.transferTime = transferApplicationPaginationItemDataDto.getTransferTime() == null?"":new DateTime(transferApplicationPaginationItemDataDto.getTransferTime()).toString("yyyy-MM-dd HH:mm:ss");
        this.baseRate = decimalFormat.format(transferApplicationPaginationItemDataDto.getBaseRate());
        this.activityRate =  decimalFormat.format(transferApplicationPaginationItemDataDto.getActivityRate());
        this.transferStatus = TransferStatus.valueOf(transferApplicationPaginationItemDataDto.getTransferStatus());
        this.leftPeriod = String.valueOf(transferApplicationPaginationItemDataDto.getLeftPeriod());
    }
}
