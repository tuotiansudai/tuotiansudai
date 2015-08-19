package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanModel {
    private String id;
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
    private String loanAmount;
    /***投资手续费比例***/
    private String investFeeRate;
    /***最小投资金额***/
    private String minInvestAmount;
    /***投资递增金额***/
    private String investIncreasingAmount;
    /***单笔最大投资金额***/
    private String maxInvestAmount;
    /***活动类型***/
    private ActivityType activityType;
    /***活动利率***/
    private String activityRate;
    /***合同***/
    private String contractId;
    /***筹款开始时间***/
    private Date fundraisingStartTime;
    /***筹款截止时间***/
    private Date fundraisingEndTime;
    /***是否显示在首页true:显示在首页，false:不显示在首页***/
    private boolean showOnHome;

    public LoanModel(){}

    public LoanModel(LoanDto loanDto) throws ParseException {
        this.name =loanDto.getProjectName();
        this.activityRate = loanDto.getActivityRate();
        this.activityType = loanDto.getActivityType();
        this.agentLoginName = loanDto.getAgentLoginName();
        this.loanLoginName = loanDto.getLoanLoginName();
        this.contractId = loanDto.getContractId();
        this.descriptionHtml = loanDto.getDescriptionHtml();
        this.descriptionText = loanDto.getDescriptionText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.fundraisingStartTime = sdf.parse(loanDto.getFundraisingStartTime());
        this.fundraisingEndTime = sdf.parse(loanDto.getFundraisingEndTime());
        this.investFeeRate = loanDto.getInvestFeeRate();
        this.investIncreasingAmount = loanDto.getInvestIncreasingAmount();
        this.maxInvestAmount = loanDto.getMaxInvestAmount();
        this.minInvestAmount = loanDto.getMinInvestAmount();
        this.periods = loanDto.getPeriods();
        this.showOnHome = loanDto.isShowOnHome();
        this.type = loanDto.getType();
        this.loanAmount = loanDto.getLoanAmount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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
}
