package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferPurchaseResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "账户余额", example = "100")
    private String balance;

    @ApiModelProperty(value = "认购金额", example = "100")
    private String transferAmount;

    @ApiModelProperty(value = "预计利息A", example = "10")
    private String expectedInterestAmount;
    @ApiModelProperty(value = "项目风险等级名称", example = "稳健型")
    public String riskEstimate;
    @ApiModelProperty(value = "项目风险等级", example = "0")
    public int estimateLevel;
    @ApiModelProperty(value = "用户风险等级", example = "稳健型")
    public String userRiskEstimate;
    @ApiModelProperty(value = "用户风险等级", example = "0")
    public int userRstimateLevel;
    @ApiModelProperty(value = "用户可用风险投资金额", example = "0")
    public long availableEstimateMoney;
    @ApiModelProperty(value = "用户风险评估限制金额", example = "0")
    public long userEstimateLimit;

    public TransferPurchaseResponseDataDto(){}

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getExpectedInterestAmount() {
        return expectedInterestAmount;
    }

    public void setExpectedInterestAmount(String expectedInterestAmount) {
        this.expectedInterestAmount = expectedInterestAmount;
    }

    public String getRiskEstimate() {
        return riskEstimate;
    }

    public void setRiskEstimate(String riskEstimate) {
        this.riskEstimate = riskEstimate;
    }

    public int getEstimateLevel() {
        return estimateLevel;
    }

    public void setEstimateLevel(int estimateLevel) {
        this.estimateLevel = estimateLevel;
    }

    public String getUserRiskEstimate() {
        return userRiskEstimate;
    }

    public void setUserRiskEstimate(String userRiskEstimate) {
        this.userRiskEstimate = userRiskEstimate;
    }

    public int getUserRstimateLevel() {
        return userRstimateLevel;
    }

    public void setUserRstimateLevel(int userRstimateLevel) {
        this.userRstimateLevel = userRstimateLevel;
    }

    public long getAvailableEstimateMoney() {
        return availableEstimateMoney;
    }

    public void setAvailableEstimateMoney(long availableEstimateMoney) {
        this.availableEstimateMoney = availableEstimateMoney;
    }

    public long getUserEstimateLimit() {
        return userEstimateLimit;
    }

    public void setUserEstimateLimit(long userEstimateLimit) {
        this.userEstimateLimit = userEstimateLimit;
    }
}
