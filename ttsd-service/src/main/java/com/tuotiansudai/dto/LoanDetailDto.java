package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

public class LoanDetailDto extends BaseDataDto {

    private long id;

    private String name;

    /***
     * 代理人
     ***/
    @NotEmpty
    private String agentLoginName;

    /***
     * 借款人ID
     ***/
    @NotEmpty
    @NotNull
    private String loanerLoginName;

    /***
     * 借款人
     ***/
    @NotEmpty
    @NotNull
    private String loanerUserName;

    /***
     * 借款人身份证
     ***/
    @NotEmpty
    @NotNull
    private String loanerIdentityNumber;

    /***
     * 标的类型
     ***/
    @NotEmpty
    private LoanType type;

    /***
     * 借款期限
     ***/
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private int periods;

    /***
     * 项目描述（纯文本）
     ***/
    @NotEmpty
    private String descriptionText;

    /***
     * 项目描述（带html标签）
     ***/
    @NotEmpty
    private String descriptionHtml;

    /***
     * 投资手续费比例
     ***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String investFeeRate;

    /***
     * 最小投资金额
     ***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String minInvestAmount;

    /***
     * 投资递增金额
     ***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String investIncreasingAmount;

    /***
     * 单笔最大投资金额
     ***/
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String maxInvestAmount;

    /***
     * 活动类型
     ***/
    @NotEmpty
    private ActivityType activityType;

    @NotEmpty
    private ProductType productType;

    /***
     * 活动利率
     ***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String activityRate;

    /***
     * 基本利率
     ***/
    @NotEmpty
    @Pattern(regexp = "^[+]?[\\d]+(([\\.]{1}[\\d]+)|([\\d]*))$")
    private String basicRate;

    /***
     * 合同
     ***/
    @NotEmpty
    private long contractId;

    /***
     * 筹款开始时间
     ***/
    @NotEmpty
    private Date fundraisingStartTime;

    /***
     * 筹款截止时间
     ***/
    @NotEmpty
    private Date fundraisingEndTime;

    /***
     * 筹款完成时间
     ***/
    private Date raisingCompleteTime;

    /***
     * 是否显示在首页 true:显示在首页，false:不显示在首页
     ***/
    private boolean showOnHome;

    /***
     * 借款金额
     ***/
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String loanAmount;

    /***
     * 建标时间
     ***/
    private Date createdTime;

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
    private LoanStatus loanStatus;

    /***
     * 申请材料
     ***/
    private List<LoanTitleRelationModel> loanTitles;

    private List<LoanTitleModel> loanTitleDto;

    /**
     * 可投金额
     **/
    private double amountNeedRaised;

    private String maxAvailableInvestAmount;

    /**
     * 当前登录用户的个人账户余额
     **/
    private double balance;

    /**
     * 完成比例
     **/
    private double raiseCompletedRate;

    /**
     * 预计总收益
     **/
    private long expectedTotalIncome;

    private long preheatSeconds;

    private String createdLoginName;

    private String verifyLoginName;

    private String recheckLoginName;

    public LoanDetailDto() {
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

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public String getLoanerIdentityNumber() {
        return loanerIdentityNumber;
    }

    public void setLoanerIdentityNumber(String loanerIdentityNumber) {
        this.loanerIdentityNumber = loanerIdentityNumber;
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

    public Date getRaisingCompleteTime() {
        return raisingCompleteTime;
    }

    public void setRaisingCompleteTime(Date raisingCompleteTime) {
        this.raisingCompleteTime = raisingCompleteTime;
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

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
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

    public List<LoanTitleModel> getLoanTitleDto() {
        return loanTitleDto;
    }

    public void setLoanTitleDto(List<LoanTitleModel> loanTitleDto) {
        this.loanTitleDto = loanTitleDto;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }

    public String getMaxAvailableInvestAmount() {
        return maxAvailableInvestAmount;
    }

    public void setMaxAvailableInvestAmount(String maxAvailableInvestAmount) {
        this.maxAvailableInvestAmount = maxAvailableInvestAmount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getCreatedLoginName() {
        return createdLoginName;
    }

    public void setCreatedLoginName(String createdLoginName) {
        this.createdLoginName = createdLoginName;
    }

    public String getVerifyLoginName() {
        return verifyLoginName;
    }

    public void setVerifyLoginName(String verifyLoginName) {
        this.verifyLoginName = verifyLoginName;
    }

    public String getRecheckLoginName() {
        return recheckLoginName;
    }

    public void setRecheckLoginName(String recheckLoginName) {
        this.recheckLoginName = recheckLoginName;
    }
}
