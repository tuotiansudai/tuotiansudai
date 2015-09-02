package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import com.tuotiansudai.repository.model.LoanType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

public class LoanDto extends BaseDataDto{

    private long id;

    /***标的名称***/
    @NotEmpty
    private String projectName;

    /***代理人***/
    @NotEmpty
    private String agentLoginName;

    /***借款用户***/
    @NotEmpty
    @NotNull
    private String loanerLoginName;

    /***标的类型***/
    @NotEmpty
    private LoanType type;

    /***借款期限***/
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private long periods;

    /***项目描述（纯文本）***/
    @NotEmpty
    private String descriptionText;

    /***项目描述（带html标签）***/
    @NotEmpty
    private String descriptionHtml;

    /***投资手续费比例***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String investFeeRate;

    /***最小投资金额***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String minInvestAmount;

    /***投资递增金额***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String investIncreasingAmount;

    /***单笔最大投资金额***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String maxInvestAmount;

    /***活动类型***/
    @NotEmpty
    private ActivityType activityType;

    /***活动利率***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String activityRate;

    /***基本利率***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String basicRate;

    /***合同***/
    @NotEmpty
    private long contractId;

    /***筹款开始时间***/
    @NotEmpty
    private Date fundraisingStartTime;

    /***筹款截止时间***/
    @NotEmpty
    private Date fundraisingEndTime;

    /***是否显示在首页 true:显示在首页，false:不显示在首页***/
    private boolean showOnHome;

    /***借款金额***/
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String loanAmount;

    /***建标时间***/
    private Date createdTime;

    /***标的状态***/
    private LoanStatus loanStatus;

    /***申请材料***/
    private List<LoanTitleRelationModel> loanTitles;

    private long preheatSeconds;

    private double amountNeedRaised;

    private double balance;

    private double raiseCompletedRate;

    private long expectedTotalIncome;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public long getPeriods() {
        return periods;
    }

    public void setPeriods(long periods) {
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

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(String investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public String getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(String basicRate) {
        this.basicRate = basicRate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
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

    public boolean isShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        this.showOnHome = showOnHome;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }

    public double getAmountNeedRaised() {
        return amountNeedRaised;
    }

    public void setAmountNeedRaised(double amountNeedRaised) {
        this.amountNeedRaised = amountNeedRaised;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
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

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
}
