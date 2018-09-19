package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.PledgeType;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LoanResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private String loanId;

    @ApiModelProperty(value = "产品线", example = "SYL")
    private String loanType;

    @ApiModelProperty(value = "产品线", example = "速盈利")
    private String loanTypeName;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "还款方式代码", example = "LOAN_INTEREST_MONTHLY_REPAY")
    private String repayTypeCode;

    @ApiModelProperty(value = "还款方式名称", example = "先付收益后还投资本金，按天计息，放款后生息")
    private String repayTypeName;

    @ApiModelProperty(value = "期数", example = "12")
    private Integer deadline;

    @ApiModelProperty(value = "期限单位", example = "日")
    private String repayUnit;

    @ApiModelProperty(value = "总年化受益", example = "10")
    private String ratePercent;

    @ApiModelProperty(value = "借款总额", example = "10000")
    private String loanMoney;

    @ApiModelProperty(value = "项目状态代码", example = "recheck,repaying,raising,complete")
    private String loanStatus;

    @ApiModelProperty(value = "项目状态描述", example = "等待复核,回款中,筹款中,已完成")
    private String loanStatusDesc;

    @ApiModelProperty(value = "已投金额", example = "10000")
    private String investedMoney;

    @ApiModelProperty(value = "基础利率", example = "10")
    private String baseRatePercent;

    @ApiModelProperty(value = "活动利率", example = "10")
    private String activityRatePercent;

    @ApiModelProperty(value = "起投金额", example = "50")
    private String minInvestMoney;

    @ApiModelProperty(value = "递增金额", example = "50")
    private String cardinalNumber;

    @ApiModelProperty(value = "投资上限", example = "100")
    private String maxInvestMoney;

    @ApiModelProperty(value = "起投时间", example = "2016-11-24 12:00:00")
    private String investBeginTime;

    @ApiModelProperty(value = "剩余秒数", example = "10")
    private String investBeginSeconds;

    @ApiModelProperty(value = "募集完成时间", example = "2016-11-24 17:00:00")
    private String raiseCompletedTime;

    @ApiModelProperty(value = "手续费比例", example = "10")
    private String investFeeRate;

    @ApiModelProperty(value = "借款天数", example = "360")
    private String duration;

    @ApiModelProperty(value = "从当前时间到借款截止时间天数", example = "360")
    private String availableDuration;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360")
    private String productNewType;

    @ApiModelProperty(value = "活动类型", example = "NORMAL")
    private String activityType;

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

    @ApiModelProperty(value = "活动利率", example = "list")
    private List<ExtraLoanRateDto> extraRates;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String extraSource;

    @ApiModelProperty(value = "标的类型", example = "NORMAL(普通投资),NEWBIE(新手专享),EXCLUSIVE(定向投资),PROMOTION(加息投资)")
    private String activityDesc;

    @ApiModelProperty(value = "抵押", example = "HOUSE")
    private PledgeType pledgeType;

    @ApiModelProperty(value = "万元收益", example = "1000")
    private String interestPerTenThousands;

    @ApiModelProperty(value = "风险评估等级，默认为Estimate.lower")
    private Integer estimateLevel;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getRepayTypeCode() {
        return repayTypeCode;
    }

    public void setRepayTypeCode(String repayTypeCode) {
        this.repayTypeCode = repayTypeCode;
    }

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getRepayUnit() {
        return repayUnit;
    }

    public void setRepayUnit(String repayUnit) {
        this.repayUnit = repayUnit;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatusDesc() {
        return loanStatusDesc;
    }

    public void setLoanStatusDesc(String loanStatusDesc) {
        this.loanStatusDesc = loanStatusDesc;
    }

    public String getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(String ratePercent) {
        this.ratePercent = ratePercent;
    }

    public String getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(String loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getInvestedMoney() {
        return investedMoney;
    }

    public void setInvestedMoney(String investedMoney) {
        this.investedMoney = investedMoney;
    }

    public String getBaseRatePercent() {
        return baseRatePercent;
    }

    public void setBaseRatePercent(String baseRatePercent) {
        this.baseRatePercent = baseRatePercent;
    }

    public String getActivityRatePercent() {
        return activityRatePercent;
    }

    public void setActivityRatePercent(String activityRatePercent) {
        this.activityRatePercent = activityRatePercent;
    }

    public String getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(String minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public String getCardinalNumber() {
        return cardinalNumber;
    }

    public void setCardinalNumber(String cardinalNumber) {
        this.cardinalNumber = cardinalNumber;
    }

    public String getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(String maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public String getInvestBeginTime() {
        return investBeginTime;
    }

    public void setInvestBeginTime(String investBeginTime) {
        this.investBeginTime = investBeginTime;
    }

    public String getInvestBeginSeconds() {
        return investBeginSeconds;
    }

    public void setInvestBeginSeconds(String investBeginSeconds) {
        this.investBeginSeconds = investBeginSeconds;
    }

    public String getRaiseCompletedTime() {
        return raiseCompletedTime;
    }

    public void setRaiseCompletedTime(String raiseCompletedTime) {
        this.raiseCompletedTime = raiseCompletedTime;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public void setLoanTypeName(String loanTypeName) {
        this.loanTypeName = loanTypeName;
    }

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public String getAvailableDuration() {
        return availableDuration;
    }

    public void setAvailableDuration(String availableDuration) {
        this.availableDuration = availableDuration;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public List<ExtraLoanRateDto> getExtraRates() {
        if (CollectionUtils.isEmpty(extraRates)) {
            return new ArrayList<>();
        }
        return extraRates;
    }

    public void setExtraRates(List<ExtraLoanRateDto> extraRates) {
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

    public String getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(String extraSource) {
        this.extraSource = extraSource;
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

    public String getInterestPerTenThousands() {
        return interestPerTenThousands;
    }

    public void setInterestPerTenThousands(String interestPerTenThousands) {
        this.interestPerTenThousands = interestPerTenThousands;
    }

    public Integer getEstimateLevel() {
        return estimateLevel;
    }

    public void setEstimateLevel(Integer estimateLevel) {
        this.estimateLevel = estimateLevel;
    }

}
