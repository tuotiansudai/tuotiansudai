package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.TransferApplicationRecordView;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.text.DecimalFormat;

public class TransferApplicationRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "债权转让ID", example = "1")
    private String transferApplicationId;

    @ApiModelProperty(value = "名称", example = "债权转让")
    private String name;

    @ApiModelProperty(value = "债权转让金额", example = "1")
    private String transferAmount;

    @ApiModelProperty(value = "出借id", example = "2")
    private Long investId;

    @ApiModelProperty(value = "债权转让出借id", example = "3")
    private Long transferInvestId;

    @ApiModelProperty(value = "出借金额", example = "100")
    private String investAmount;

    @ApiModelProperty(value = "转让时间", example = "2016-11-25 15:12:34")
    private String transferTime;

    @ApiModelProperty(value = "基本利率", example = "10")
    private String baseRate;

    @ApiModelProperty(value = "活动利率", example = "2")
    private String activityRate;

    @ApiModelProperty(value = "债权转让状态", example = "TRANSFERRING")
    private TransferStatus transferStatus;

    @ApiModelProperty(value = "剩余期数", example = "1")
    private String leftPeriod;

    @ApiModelProperty(value = "剩余天数", example = "30")
    private String leftDays;

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

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

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public Long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(Long transferInvestId) {
        this.transferInvestId = transferInvestId;
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

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public TransferApplicationRecordResponseDataDto() {

    }

    public TransferApplicationRecordResponseDataDto(TransferApplicationRecordView transferApplicationRecordView) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.transferApplicationId = String.valueOf(transferApplicationRecordView.getTransferApplicationId());
        this.name = transferApplicationRecordView.getName();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordView.getTransferAmount());
        this.investId = transferApplicationRecordView.getInvestId();
        this.transferInvestId = transferApplicationRecordView.getTransferInvestId();
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordView.getInvestAmount());
        this.transferTime = transferApplicationRecordView.getTransferTime() == null ? "" : new DateTime(transferApplicationRecordView.getTransferTime()).toString("yyyy-MM-dd HH:mm:ss");
        this.baseRate = decimalFormat.format(transferApplicationRecordView.getBaseRate() * 100);
        this.activityRate = decimalFormat.format(transferApplicationRecordView.getActivityRate() * 100);
        this.transferStatus = transferApplicationRecordView.getTransferStatus();
        this.leftPeriod = String.valueOf(transferApplicationRecordView.getLeftPeriod());
    }

    public TransferApplicationRecordResponseDataDto(TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.transferApplicationId = String.valueOf(transferApplicationPaginationItemDataDto.getTransferApplicationId());
        this.name = transferApplicationPaginationItemDataDto.getName();
        this.transferAmount = transferApplicationPaginationItemDataDto.getTransferAmount();
        this.investAmount = transferApplicationPaginationItemDataDto.getInvestAmount();
        this.transferTime = transferApplicationPaginationItemDataDto.getTransferTime() == null ? "" : new DateTime(transferApplicationPaginationItemDataDto.getTransferTime()).toString("yyyy-MM-dd HH:mm:ss");
        this.baseRate = decimalFormat.format(transferApplicationPaginationItemDataDto.getBaseRate());
        this.activityRate = decimalFormat.format(transferApplicationPaginationItemDataDto.getActivityRate());
        this.transferStatus = TransferStatus.valueOf(transferApplicationPaginationItemDataDto.getTransferStatus());
        this.leftPeriod = String.valueOf(transferApplicationPaginationItemDataDto.getLeftPeriod());
        this.leftDays = transferApplicationPaginationItemDataDto.getLeftDays();
    }
}
