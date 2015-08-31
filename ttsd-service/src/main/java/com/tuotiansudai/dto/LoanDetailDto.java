package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import java.util.Date;
import java.util.List;

public class LoanDetailDto extends BaseDataDto{
    private long id;
    private String name;
    private String agentLoginName;
    private String loanerLoginName;
    private String loanTypeDesc;
    private String periods;
    private String repayUnit;
    private String descriptionText;
    private String descriptionHtml;
    private long loanAmount;
    private long minInvestAmount;
    private long investIncreasingAmount;
    private long maxInvestAmount;
    private ActivityType activityType;
    private double activityRate;
    private double basicRate;
    private Date fundraisingStartTime;
    private long preheatSeconds;
    private Date fundraisingEndTime;
    private LoanStatus loanStatus;
    private long amountNeedRaised;
    private long balance;
    private double raiseCompletedRate;
    private long expectedTotalIncome;
    private List<ApplyMaterialDto> applyMetarial;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public String getLoanerLoginName() {
        return loanerLoginName;
    }

    public void setLoanerLoginName(String loanerLoginName) {
        this.loanerLoginName = loanerLoginName;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public long getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(long investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
    }

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public double getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(double basicRate) {
        this.basicRate = basicRate;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(Date fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public Date getFundraisingEndTime() {
        return fundraisingEndTime;
    }

    public void setFundraisingEndTime(Date fundraisingEndTime) {
        this.fundraisingEndTime = fundraisingEndTime;
    }

    public String getLoanTypeDesc() {
        return loanTypeDesc;
    }

    public void setLoanTypeDesc(String loanTypeDesc) {
        this.loanTypeDesc = loanTypeDesc;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public long getAmountNeedRaised() {
        return amountNeedRaised;
    }

    public void setAmountNeedRaised(long amountNeedRaised) {
        this.amountNeedRaised = amountNeedRaised;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public double getRaiseCompletedRate() {
        return raiseCompletedRate;
    }

    public void setRaiseCompletedRate(double raiseCompletedRate) {
        this.raiseCompletedRate = raiseCompletedRate;
    }

    public long getExpectedTotalIncome() {
        return expectedTotalIncome;
    }

    public void setExpectedTotalIncome(long expectedTotalIncome) {
        this.expectedTotalIncome = expectedTotalIncome;
    }

    public List<ApplyMaterialDto> getApplyMetarial() {
        return applyMetarial;
    }

    public void setApplyMetarial(List<ApplyMaterialDto> applyMetarial) {
        this.applyMetarial = applyMetarial;
    }

    public String getRepayUnit() {
        return repayUnit;
    }

    public void setRepayUnit(String repayUnit) {
        this.repayUnit = repayUnit;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }
}
