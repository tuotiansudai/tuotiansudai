package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDto;

import java.util.Date;

public class LoanModel {
    /***标的号***/
    private long id;
    /***借款项目名称***/
    private String name;
    /***代理人***/
    private String agentLoginName;
    /***借款用户***/
    private String loanLoginName;
    /***标的类型***/
    private LoanType type;
    /***借款期限***/
    private String periods;
    /***项目描述（纯文本）***/
    private String descriptionText;
    /***项目描述（带html标签）***/
    private String descriptionHtml;
    /***借款金额***/
    private long loanAmount;
    /***投资手续费比例***/
    private double investFeeRate;
    /***最小投资金额***/
    private long minInvestAmount;
    /***投资递增金额***/
    private long investIncreasingAmount;
    /***单笔最大投资金额***/
    private long maxInvestAmount;
    /***活动类型***/
    private ActivityType activityType;
    /***活动利率***/
    private double activityRate;
    /***基本利率***/
    private double basicRate;
    /***合同***/
    private long contractId;
    /***筹款开始时间***/
    private Date fundraisingStartTime;
    /***筹款截止时间***/
    private Date fundraisingEndTime;
    /***是否显示在首页true:显示在首页，false:不显示在首页***/
    private boolean showOnHome;
    /***建标时间***/
    private Date createdTime;
    /***标的状态***/
    private LoanStatus status;

    public LoanModel(){}

    public LoanModel(LoanDto loanDto) {
        this.id = loanDto.getId();
        this.name =loanDto.getProjectName();
        this.activityRate = Double.parseDouble(loanDto.getActivityRate());
        this.basicRate = Double.parseDouble(loanDto.getBasicRate());
        this.activityType = loanDto.getActivityType();
        this.agentLoginName = loanDto.getAgentLoginName();
        this.loanLoginName = loanDto.getLoanerLoginName();
        this.contractId = loanDto.getContractId();
        this.descriptionHtml = loanDto.getDescriptionHtml();
        this.descriptionText = loanDto.getDescriptionText();
        this.fundraisingStartTime = loanDto.getFundraisingStartTime();
        this.fundraisingEndTime = loanDto.getFundraisingEndTime();
        this.investFeeRate = Double.parseDouble(loanDto.getInvestFeeRate());
        this.investIncreasingAmount = Long.parseLong(loanDto.getInvestIncreasingAmount());
        this.maxInvestAmount = Long.parseLong(loanDto.getMaxInvestAmount());
        this.minInvestAmount = Long.parseLong(loanDto.getMinInvestAmount());
        this.periods = loanDto.getPeriods();
        this.showOnHome = loanDto.isShowOnHome();
        this.type = loanDto.getType();
        this.loanAmount = Long.parseLong(loanDto.getLoanAmount());
        this.createdTime = loanDto.getCreatedTime();
        this.status = loanDto.getStatus();
    }

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

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(double investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public Long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(Long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public Long getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(Long investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
    }

    public Long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(Long maxInvestAmount) {
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
