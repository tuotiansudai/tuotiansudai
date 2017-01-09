package com.tuotiansudai.api.dto.v2_0;

import com.tuotiansudai.repository.model.PledgeType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class LoanResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private String loanId;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "活动类型", example = "NORMAL")
    private String activityType;

    @ApiModelProperty(value = "借款天数", example = "360")
    private String duration;

    @ApiModelProperty(value = "基础利率", example = "10")
    private String baseRatePercent;

    @ApiModelProperty(value = "活动利率", example = "10")
    private String activityRatePercent;

    @ApiModelProperty(value = "标的金额", example = "100")
    private String loanAmount;

    @ApiModelProperty(value = "投资金额", example = "100")
    private String investAmount;

    @ApiModelProperty(value = "项目状态代码", example = "recheck,repaying,raising,complete")
    private String loanStatus;

    @ApiModelProperty(value = "项目状态描述", example = "等待复核,回款中,筹款中,已完成")
    private String loanStatusDesc;

    @ApiModelProperty(value = "筹款开始时间", example = "2016-11-25 17:55:03")
    private String fundraisingStartTime;

    @ApiModelProperty(value = "筹款开始时间", example = "100")
    private String fundraisingCountDown;

    @ApiModelProperty(value = "起投金额", example = "50")
    private String minInvestMoney;

    @ApiModelProperty(value = "递增金额", example = "50")
    private String cardinalNumber;

    @ApiModelProperty(value = "投资上限", example = "100")
    private String maxInvestMoney;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360")
    private String productNewType;

    @ApiModelProperty(value = "手续费比例", example = "10")
    private String investFeeRate;

    @ApiModelProperty(value = "起投金额", example = "50")
    public String minInvestMoneyCent;

    @ApiModelProperty(value = "递增金额", example = "50")
    public String cardinalNumberCent;

    @ApiModelProperty(value = "投资上限", example = "100")
    public String maxInvestMoneyCent;

    @ApiModelProperty(value = "投资总额", example = "10000")
    public String investedMoneyCent;

    @ApiModelProperty(value = "借款总额", example = "10000")
    public String loanMoneyCent;

    @ApiModelProperty(value = "阶梯加息", example = "list")
    private List<ExtraRateListResponseDataDto> extraRates;

    @ApiModelProperty(value = "标的类型", example = "NORMAL(普通投资),NEWBIE(新手专享),EXCLUSIVE(定向投资),PROMOTION(加息投资)")
    private String activityDesc;

    @ApiModelProperty(value = "抵押", example = "HOUSE,VEHICLE,ENTERPRISE,NONE")
    private PledgeType pledgeType;

    public String getLoanId() { return loanId; }

    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getLoanName() { return loanName; }

    public void setLoanName(String loanName) { this.loanName = loanName; }

    public String getActivityType() { return activityType; }

    public void setActivityType(String activityType) { this.activityType = activityType; }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public String getBaseRatePercent() { return baseRatePercent; }

    public void setBaseRatePercent(String baseRatePercent) { this.baseRatePercent = baseRatePercent; }

    public String getActivityRatePercent() { return activityRatePercent; }

    public void setActivityRatePercent(String activityRatePercent) { this.activityRatePercent = activityRatePercent; }

    public String getLoanAmount() { return loanAmount; }

    public void setLoanAmount(String loanAmount) { this.loanAmount = loanAmount; }

    public String getInvestAmount() { return investAmount; }

    public void setInvestAmount(String investAmount) { this.investAmount = investAmount; }

    public String getLoanStatus() { return loanStatus; }

    public void setLoanStatus(String loanStatus) { this.loanStatus = loanStatus; }

    public String getLoanStatusDesc() { return loanStatusDesc; }

    public void setLoanStatusDesc(String loanStatusDesc) { this.loanStatusDesc = loanStatusDesc; }

    public String getFundraisingStartTime() { return fundraisingStartTime; }

    public void setFundraisingStartTime(String fundraisingStartTime) { this.fundraisingStartTime = fundraisingStartTime; }

    public String getFundraisingCountDown() { return fundraisingCountDown; }

    public void setFundraisingCountDown(String fundraisingCountDown) { this.fundraisingCountDown = fundraisingCountDown; }

    public String getMinInvestMoney() { return minInvestMoney; }

    public void setMinInvestMoney(String minInvestMoney) { this.minInvestMoney = minInvestMoney; }

    public String getCardinalNumber() { return cardinalNumber; }

    public void setCardinalNumber(String cardinalNumber) { this.cardinalNumber = cardinalNumber; }

    public String getMaxInvestMoney() { return maxInvestMoney; }

    public void setMaxInvestMoney(String maxInvestMoney) { this.maxInvestMoney = maxInvestMoney; }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public List<ExtraRateListResponseDataDto> getExtraRates() {
        return extraRates;
    }

    public void setExtraRates(List<ExtraRateListResponseDataDto> extraRates) {
        this.extraRates = extraRates;
    }

    public String getMinInvestMoneyCent() {
        return minInvestMoneyCent;
    }

    public void setMinInvestMoneyCent(String minInvestMoneyCent) {
        this.minInvestMoneyCent = minInvestMoneyCent;
    }

    public String getCardinalNumberCent() {
        return cardinalNumberCent;
    }

    public void setCardinalNumberCent(String cardinalNumberCent) {
        this.cardinalNumberCent = cardinalNumberCent;
    }

    public String getMaxInvestMoneyCent() {
        return maxInvestMoneyCent;
    }

    public void setMaxInvestMoneyCent(String maxInvestMoneyCent) {
        this.maxInvestMoneyCent = maxInvestMoneyCent;
    }

    public String getInvestedMoneyCent() {
        return investedMoneyCent;
    }

    public void setInvestedMoneyCent(String investedMoneyCent) {
        this.investedMoneyCent = investedMoneyCent;
    }

    public String getLoanMoneyCent() {
        return loanMoneyCent;
    }

    public void setLoanMoneyCent(String loanMoneyCent) {
        this.loanMoneyCent = loanMoneyCent;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

}
