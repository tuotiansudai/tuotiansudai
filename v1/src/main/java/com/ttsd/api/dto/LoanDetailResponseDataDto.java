package com.ttsd.api.dto;

import java.util.List;

public class LoanDetailResponseDataDto extends BaseResponseDataDto{
    private String loanId;
    private String loanType;
    private String loanName;
    private String repayTypeCode;
    private String repayTypeName;
    private Integer deadline;
    private String repayUnit;
    private Double ratePercent;
    private Double loanMoney;
    private String loanStatus;
    private String loanStatusDesc;
    private Double investedMoney;
    private Double baseRatePercent;
    private Double activityRatePercent;
    private Long investedCount;
    private String description;
    private Integer investCount;
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

    public Double getBaseRatePercent() {
        return baseRatePercent;
    }

    public void setBaseRatePercent(Double baseRatePercent) {
        this.baseRatePercent = baseRatePercent;
    }

    public Double getActivityRatePercent() {
        return activityRatePercent;
    }

    public void setActivityRatePercent(Double activityRatePercent) {
        this.activityRatePercent = activityRatePercent;
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

    public Double getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(Double ratePercent) {
        this.ratePercent = ratePercent;
    }

    public Double getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(Double loanMoney) {
        this.loanMoney = loanMoney;
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

    public Double getInvestedMoney() {
        return investedMoney;
    }

    public void setInvestedMoney(Double investedMoney) {
        this.investedMoney = investedMoney;
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
