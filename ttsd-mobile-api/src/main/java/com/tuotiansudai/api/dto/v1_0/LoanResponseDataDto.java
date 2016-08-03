package com.tuotiansudai.api.dto.v1_0;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LoanResponseDataDto {
    private String loanId;
    private String loanType;
    private String loanTypeName;
    private String loanName;
    private String repayTypeCode;
    private String repayTypeName;
    private Integer deadline;
    private String repayUnit;
    private String ratePercent;
    private String loanMoney;
    private String loanStatus;
    private String loanStatusDesc;
    private String investedMoney;
    private String baseRatePercent;
    private String activityRatePercent;
    private String minInvestMoney;
    private String cardinalNumber;
    private String maxInvestMoney;
    private String investBeginTime;
    private String investBeginSeconds;
    private String raiseCompletedTime;
    private String investFeeRate;
    private String duration;
    private String productNewType;
    private String activityType;
    private List<ExtraLoanRateDto> extraRates;

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
}
