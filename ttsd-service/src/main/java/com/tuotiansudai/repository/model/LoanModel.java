package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.utils.AmountUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LoanModel {
    /***
     * 标的号
     ***/
    private long id;
    /***
     * 借款项目名称
     ***/
    private String name;
    /***
     * 代理人
     ***/
    private String agentLoginName;
    /***
     * 借款用户
     ***/
    private String loanerLoginName;
    /***
     * 标的类型
     ***/
    private LoanType type;
    /***
     * 借款期限
     ***/
    private int periods;
    /***
     * 项目描述（纯文本）
     ***/
    private String descriptionText;
    /***
     * 项目描述（带html标签）
     ***/
    private String descriptionHtml;
    /***
     * 借款金额
     ***/
    private long loanAmount;
    /***
     * 投资手续费比例
     ***/
    private double investFeeRate;
    /***
     * 最小投资金额
     ***/
    private long minInvestAmount;
    /***
     * 投资递增金额
     ***/
    private long investIncreasingAmount;
    /***
     * 单笔最大投资金额
     ***/
    private long maxInvestAmount;
    /***
     * 活动类型
     ***/
    private ActivityType activityType;
    /***
     * 活动利率
     ***/
    private double activityRate;
    /***
     * 基本利率
     ***/
    private double baseRate;
    /***
     * 合同
     ***/
    private long contractId;
    /***
     * 筹款开始时间
     ***/
    private Date fundraisingStartTime;
    /***
     * 筹款截止时间
     ***/
    private Date fundraisingEndTime;
    /***
     * 是否显示在首页true:显示在首页，false:不显示在首页
     ***/
    private boolean showOnHome;
    /***
     * 建标时间
     ***/
    private Date createdTime = new Date();
    /***
     * 初审时间
     ***/
    private Date verifyTime;
    /***
     * 复审时间
     ***/
    private Date recheckTime;
    /***
     * 标的状态
     ***/
    private LoanStatus status;

    /***
     * 申请材料
     ***/
    private List<LoanTitleRelationModel> loanTitles;

    /**
     * 还款日
     */
    private Date nextRepayDate;

    /**
     * 完成日期
     */
    private Date completedDate;

    /**
     * 应还总额
     */
    private long expectedRepayAmount;

    /**
     * 实还总额
     */
    private long actualRepayAmount;

    /**
     * 待还总额
     */
    private long unpaidAmount;

    public LoanModel() {
    }

    public LoanModel(LoanDto loanDto) {
        this.id = loanDto.getId();
        this.name = loanDto.getProjectName();
        this.activityRate = Double.parseDouble(rateStrDivideOneHundred(loanDto.getActivityRate()));
        this.investFeeRate = Double.parseDouble(rateStrDivideOneHundred(loanDto.getInvestFeeRate()));
        this.baseRate = Double.parseDouble(rateStrDivideOneHundred(loanDto.getBasicRate()));
        this.activityType = loanDto.getActivityType();
        this.agentLoginName = loanDto.getAgentLoginName();
        this.loanerLoginName = loanDto.getLoanerLoginName();
        this.contractId = loanDto.getContractId();
        this.descriptionHtml = loanDto.getDescriptionHtml();
        this.descriptionText = loanDto.getDescriptionText();
        this.fundraisingStartTime = loanDto.getFundraisingStartTime();
        this.fundraisingEndTime = loanDto.getFundraisingEndTime();
        this.investIncreasingAmount = AmountUtil.convertStringToCent(loanDto.getInvestIncreasingAmount());
        this.maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        this.minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        this.periods = loanDto.getPeriods();
        this.showOnHome = loanDto.isShowOnHome();
        this.type = loanDto.getType();
        this.loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        this.status = LoanStatus.WAITING_VERIFY;
        this.verifyTime = loanDto.getVerifyTime();
        this.recheckTime = loanDto.getRecheckTime();
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

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
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

    public double getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(double investFeeRate) {
        this.investFeeRate = investFeeRate;
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

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
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

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public long getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public long getActualRepayAmount() {
        return actualRepayAmount;
    }

    public long getUnpaidAmount() {
        return unpaidAmount;
    }

    public Date getCanceledDate() {
        if (LoanStatus.CANCEL == status) {
            return recheckTime;
        }
        return null;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public void setExpectedRepayAmount(long expectedRepayAmount) {
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public void setActualRepayAmount(long actualRepayAmount) {
        this.actualRepayAmount = actualRepayAmount;
    }

    public void setUnpaidAmount(long unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    private String rateStrDivideOneHundred(String rate) {
        BigDecimal rateBigDecimal = new BigDecimal(rate);
        return String.valueOf(rateBigDecimal.divide(new BigDecimal(100), 4, BigDecimal.ROUND_DOWN).doubleValue());
    }

    public int calculateLoanRepayTimes() {
        return LoanPeriodUnit.DAY == this.type.getLoanPeriodUnit() ? 1 : this.periods;
    }
}
