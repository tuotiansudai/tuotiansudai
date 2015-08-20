package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;

public class LoanDto {

    private String id;

    /***标的名称***/
    @NotEmpty
    private String projectName;

    /***代理人***/
    @NotEmpty
    private String agentLoginName;

    /***借款用户***/
    @NotEmpty
    private String loanLoginName;

    /***标的类型***/
    @NotEmpty
    private LoanType type;

    /***借款期限***/
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String periods;

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
    @Pattern(regexp = "^\\d+$")
    private String minInvestAmount;

    /***投资递增金额***/
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String investIncreasingAmount;

    /***单笔最大投资金额***/
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String maxInvestAmount;

    /***活动类型***/
    @NotEmpty
    private ActivityType activityType;

    /***活动利率***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String activityRate;

    /***合同***/
    @NotEmpty
    private String contractId;

    /***筹款开始时间***/
    @NotEmpty
    private String fundraisingStartTime;

    /***筹款截止时间***/
    @NotEmpty
    private String fundraisingEndTime;

    /***是否显示在首页true:显示在首页，false:不显示在首页***/
    private boolean showOnHome;

    /***借款金额***/
    @Pattern(regexp = "^\\d+$")
    private String loanAmount;

    /***申请材料***/
    private List<LoanTitleModel> loanTitles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getLoanLoginName() {
        return loanLoginName;
    }

    public void setLoanLoginName(String loanLoginName) {
        this.loanLoginName = loanLoginName;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
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

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(String fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public String getFundraisingEndTime() {
        return fundraisingEndTime;
    }

    public void setFundraisingEndTime(String fundraisingEndTime) {
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

    public List<LoanTitleModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleModel> loanTitles) {
        this.loanTitles = loanTitles;
    }
}
