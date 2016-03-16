package com.tuotiansudai.api.dto;

import java.util.List;

public class LoanDetailResponseDataDto extends BaseResponseDataDto {
    private String loanId;
    private String loanType;
    private String loanName;
    private String repayTypeCode;
    private String repayTypeName;
    private Integer periods;
    private Integer deadline;
    private String repayUnit;
    private String ratePercent;
    private String loanMoney;
    private String loanStatus;
    private String loanStatusDesc;
    private String investedMoney;
    private String baseRatePercent;
    private String activityRatePercent;
    private Long investedCount;
    private String loanDetail;
    private Integer investCount;
    private String agent;
    private String loaner;
    private String verifyTime;
    private String remainTime;
    private String investBeginTime;
    private String investBeginSeconds;
    private String fundRaisingEndTime;
    private String minInvestMoney;
    private String cardinalNumber;
    private String maxInvestMoney;
    private String raiseCompletedTime;
    private String interestPointName;
    private String investFeeRate;


    private List<EvidenceResponseDataDto> evidence;

    private List<InvestRecordResponseDataDto> investRecord;

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

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
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

    public Long getInvestedCount() {
        return investedCount;
    }

    public void setInvestedCount(Long investedCount) {
        this.investedCount = investedCount;
    }

    public Integer getInvestCount() {
        return investCount;
    }

    public void setInvestCount(Integer investCount) {
        this.investCount = investCount;
    }

    public String getLoanDetail() {
        return loanDetail;
    }

    public void setLoanDetail(String loanDetail) {
        this.loanDetail = loanDetail;
    }

    public List<EvidenceResponseDataDto> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceResponseDataDto> evidence) {
        this.evidence = evidence;
    }

    public List<InvestRecordResponseDataDto> getInvestRecord() {
        return investRecord;
    }

    public void setInvestRecord(List<InvestRecordResponseDataDto> investRecord) {
        this.investRecord = investRecord;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getLoaner() {
        return loaner;
    }

    public void setLoaner(String loaner) {
        this.loaner = loaner;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
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

    public String getRaiseCompletedTime() {
        return raiseCompletedTime;
    }

    public void setRaiseCompletedTime(String raiseCompletedTime) {
        this.raiseCompletedTime = raiseCompletedTime;
    }

    public String getInterestPointName() {
        return interestPointName;
    }

    public void setInterestPointName(String interestPointName) {
        this.interestPointName = interestPointName;
    }

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public String getFundRaisingEndTime() {
        return fundRaisingEndTime;
    }

    public void setFundRaisingEndTime(String fundRaisingEndTime) {
        this.fundRaisingEndTime = fundRaisingEndTime;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }
}
