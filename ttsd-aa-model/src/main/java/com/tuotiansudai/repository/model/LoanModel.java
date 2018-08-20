package com.tuotiansudai.repository.model;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanCreateBaseRequestDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LoanModel implements Serializable {

    private long id; // 标的号

    private String name; // 借款项目名称

    private String agentLoginName; // 代理人

    private String loanerLoginName;

    private String loanerUserName; // 借款人

    private String loanerIdentityNumber; //借款人身份证号

    private LoanType type; // 标的类型

    private int periods; // 借款期数

    private int originalDuration; // 原始借款期限

    private int duration; // 借款天数

    private String descriptionText; // 项目描述（纯文本）

    private String descriptionHtml; // 项目描述（带html标签）

    private PledgeType pledgeType; // 抵押物类型

    private long loanAmount; // 借款金额

    private long minInvestAmount; // 最小投资金额

    private long maxInvestAmount; // 单笔最大投资金额

    private long investIncreasingAmount; // 投资递增金额

    private ActivityType activityType; // 活动类型

    private ProductType productType; //产品线

    private double baseRate; // 基本利率

    private double activityRate; // 活动利率

    private long contractId; // 合同

    private Date fundraisingStartTime; // 筹款开始时间

    private Date fundraisingEndTime; // 筹款截止时间

    private Date deadline; // 借款截止时间

    private Date raisingCompleteTime; // 筹款完成时间

    private Date verifyTime; // 初审时间

    private String verifyLoginName;

    private Date recheckTime; // 复审时间

    private String recheckLoginName;

    private LoanStatus status; // 标的状态

    private Long firstInvestAchievementId; //拓荒先锋

    private Long maxAmountAchievementId; //标王

    private Long lastInvestAchievementId; //一锤定音

    private boolean showOnHome; // 是否显示在首页true:显示在首页，false:不显示在首页

    private Date createdTime; // 建标时间

    private String createdLoginName;

    private Date updateTime;

    private String loanTxNo; //银行标的号

    private String loanAccNo; //银行标的账户

    private String bankOrderNo; //银行开标订单

    private String bankOrderDate; //银行开标订单日期

    private String loanFullBankOrderNo; //银行放款订单

    private String loanFullBankOrderDate; //银行放款订单日期

    private Date nextRepayDate; //还款日

    private Date completedDate; //完成日期

    private long expectedRepayAmount; //应还总额

    private long actualRepayAmount; //实还总额

    private long unpaidAmount; // 待还总额

    private boolean isBankPlatform;//资金平台

    private long loanFee;//借款手续费

    public LoanModel() {
    }

    public LoanModel(long loanId, LoanCreateRequestDto loanCreateRequestDto) {
        LoanCreateBaseRequestDto baseRequestDto = loanCreateRequestDto.getLoan();
        this.id = loanId;
        this.name = baseRequestDto.getName();
        this.agentLoginName = baseRequestDto.getAgent();
        this.loanerUserName = loanCreateRequestDto.getLoanerDetails() != null ? loanCreateRequestDto.getLoanerDetails().getUserName() : "";
        this.loanerIdentityNumber = loanCreateRequestDto.getLoanerDetails() != null ? loanCreateRequestDto.getLoanerDetails().getIdentityNumber() : "";
        this.productType = baseRequestDto.getProductType();
        this.pledgeType = baseRequestDto.getPledgeType();
        this.type = baseRequestDto.getLoanType();
        this.activityType = baseRequestDto.getActivityType();
        this.loanAmount = AmountConverter.convertStringToCent(baseRequestDto.getLoanAmount());
        this.baseRate = Double.parseDouble(rateStrDivideOneHundred(baseRequestDto.getBaseRate()));
        this.activityRate = Double.parseDouble(rateStrDivideOneHundred(baseRequestDto.getActivityRate()));
        this.fundraisingStartTime = baseRequestDto.getFundraisingStartTime();
        this.fundraisingEndTime = baseRequestDto.getFundraisingEndTime();
        this.deadline = new DateTime(baseRequestDto.getDeadline()).plusDays(1).minusSeconds(1).toDate();
        this.investIncreasingAmount = AmountConverter.convertStringToCent(baseRequestDto.getInvestIncreasingAmount());
        this.maxInvestAmount = AmountConverter.convertStringToCent(baseRequestDto.getMaxInvestAmount());
        this.minInvestAmount = AmountConverter.convertStringToCent(baseRequestDto.getMinInvestAmount());
        this.periods = baseRequestDto.getLoanType().getLoanPeriodUnit() == LoanPeriodUnit.DAY ? 1 : baseRequestDto.getProductType().getPeriods();
        this.originalDuration = baseRequestDto.getOriginalDuration();
        this.duration = Days.daysBetween(new DateTime(this.fundraisingStartTime).withTimeAtStartOfDay(), new DateTime(this.deadline).withTimeAtStartOfDay()).getDays() + 1;
        this.status = loanCreateRequestDto.getLoan().getStatus();
        this.createdLoginName = baseRequestDto.getCreatedBy();
        this.contractId = baseRequestDto.getContractId();
        this.loanerLoginName = "";
        this.descriptionHtml = "";
        this.descriptionText = "";
        this.showOnHome = true;
        this.loanFee=AmountConverter.convertStringToCent(baseRequestDto.getLoanFee());
    }

    public LoanModel updateLoan(LoanCreateRequestDto loanCreateRequestDto) {
        LoanCreateBaseRequestDto baseRequestDto = loanCreateRequestDto.getLoan();
        this.name = this.status == LoanStatus.WAITING_VERIFY ? baseRequestDto.getName() : this.name;
        this.agentLoginName = this.status == LoanStatus.WAITING_VERIFY ? baseRequestDto.getAgent() : this.agentLoginName;
        this.loanerUserName = loanCreateRequestDto.getLoanerDetails() != null ? loanCreateRequestDto.getLoanerDetails().getUserName() : "";
        this.loanerIdentityNumber = loanCreateRequestDto.getLoanerDetails() != null ? loanCreateRequestDto.getLoanerDetails().getIdentityNumber() : "";
        this.productType = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? baseRequestDto.getProductType() : this.productType;
        this.pledgeType = this.status == LoanStatus.WAITING_VERIFY ? baseRequestDto.getPledgeType() : this.pledgeType;
        this.type = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? baseRequestDto.getLoanType() : this.type;
        this.activityType = baseRequestDto.getActivityType();
        this.loanAmount = this.status == LoanStatus.WAITING_VERIFY ? AmountConverter.convertStringToCent(baseRequestDto.getLoanAmount()) : this.loanAmount;
        this.baseRate = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? Double.parseDouble(rateStrDivideOneHundred(baseRequestDto.getBaseRate())) : this.baseRate;
        this.activityRate = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? Double.parseDouble(rateStrDivideOneHundred(baseRequestDto.getActivityRate())) : this.activityRate;
        this.fundraisingStartTime = this.status == LoanStatus.WAITING_VERIFY ? baseRequestDto.getFundraisingStartTime() : this.getFundraisingStartTime();
        this.fundraisingEndTime = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT, LoanStatus.RAISING, LoanStatus.RECHECK).contains(this.status) ? baseRequestDto.getFundraisingEndTime() : this.getFundraisingEndTime();
        this.deadline = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? new DateTime(baseRequestDto.getDeadline()).plusDays(1).minusSeconds(1).toDate() : this.deadline;
        this.investIncreasingAmount = AmountConverter.convertStringToCent(baseRequestDto.getInvestIncreasingAmount());
        this.minInvestAmount = AmountConverter.convertStringToCent(baseRequestDto.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertStringToCent(baseRequestDto.getMaxInvestAmount());
        this.periods = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? (baseRequestDto.getLoanType().getLoanPeriodUnit() == LoanPeriodUnit.DAY ? 1 : baseRequestDto.getProductType().getPeriods()) : this.periods;
        this.originalDuration = baseRequestDto.getOriginalDuration();
        this.duration = Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(this.status) ? this.duration = Days.daysBetween(new DateTime(this.fundraisingStartTime).withTimeAtStartOfDay(), new DateTime(this.deadline).withTimeAtStartOfDay()).getDays() + 1 : this.duration;
        this.contractId = baseRequestDto.getContractId();
        this.loanFee=AmountConverter.convertStringToCent(baseRequestDto.getLoanFee());
        return this;
    }

    public LoanModel(LoanDto loanDto) {
        this.id = loanDto.getId();
        this.name = loanDto.getProjectName();
        this.activityRate = Double.parseDouble(rateStrDivideOneHundred(loanDto.getActivityRate()));
        this.baseRate = Double.parseDouble(rateStrDivideOneHundred(loanDto.getBasicRate()));
        this.activityType = loanDto.getActivityType();
        this.productType = loanDto.getProductType();
        this.agentLoginName = loanDto.getAgentLoginName();
        this.loanerLoginName = loanDto.getLoanerLoginName();
        this.loanerUserName = loanDto.getLoanerUserName();
        this.loanerIdentityNumber = loanDto.getLoanerIdentityNumber();
        this.contractId = loanDto.getContractId();
        this.descriptionHtml = loanDto.getDescriptionHtml();
        this.descriptionText = loanDto.getDescriptionText();
        this.pledgeType = loanDto.getPledgeType();
        this.fundraisingStartTime = loanDto.getFundraisingStartTime();
        this.fundraisingEndTime = loanDto.getFundraisingEndTime();
        this.investIncreasingAmount = AmountConverter.convertStringToCent(loanDto.getInvestIncreasingAmount());
        this.maxInvestAmount = AmountConverter.convertStringToCent(loanDto.getMaxInvestAmount());
        this.minInvestAmount = AmountConverter.convertStringToCent(loanDto.getMinInvestAmount());
        this.periods = loanDto.getType().getLoanPeriodUnit() == LoanPeriodUnit.DAY ? 1 : loanDto.getProductType().getPeriods();
        this.duration = loanDto.getProductType().getDuration();
        this.showOnHome = loanDto.isShowOnHome();
        this.type = loanDto.getType();
        this.loanAmount = AmountConverter.convertStringToCent(loanDto.getLoanAmount());
        this.status = LoanStatus.WAITING_VERIFY;
        this.verifyTime = loanDto.getVerifyTime();
        this.recheckTime = loanDto.getRecheckTime();
        this.createdLoginName = loanDto.getCreatedLoginName();
        this.verifyLoginName = loanDto.getVerifyLoginName();
        this.recheckLoginName = loanDto.getRecheckLoginName();
        this.isBankPlatform = loanDto.getIsBankPlatform();
    }

    private String rateStrDivideOneHundred(String rate) {
        BigDecimal rateBigDecimal = new BigDecimal(rate);
        return String.valueOf(rateBigDecimal.divide(new BigDecimal(100), 4, BigDecimal.ROUND_DOWN).doubleValue());
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

    public int getOriginalDuration() {
        return originalDuration;
    }

    public void setOriginalDuration(int originalDuration) {
        this.originalDuration = originalDuration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
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

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public long getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(long investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getRaisingCompleteTime() {
        return raisingCompleteTime;
    }

    public void setRaisingCompleteTime(Date raisingCompleteTime) {
        this.raisingCompleteTime = raisingCompleteTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyLoginName() {
        return verifyLoginName;
    }

    public void setVerifyLoginName(String verifyLoginName) {
        this.verifyLoginName = verifyLoginName;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getRecheckLoginName() {
        return recheckLoginName;
    }

    public void setRecheckLoginName(String recheckLoginName) {
        this.recheckLoginName = recheckLoginName;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Long getFirstInvestAchievementId() {
        return firstInvestAchievementId;
    }

    public void setFirstInvestAchievementId(Long firstInvestAchievementId) {
        this.firstInvestAchievementId = firstInvestAchievementId;
    }

    public Long getMaxAmountAchievementId() {
        return maxAmountAchievementId;
    }

    public void setMaxAmountAchievementId(Long maxAmountAchievementId) {
        this.maxAmountAchievementId = maxAmountAchievementId;
    }

    public Long getLastInvestAchievementId() {
        return lastInvestAchievementId;
    }

    public void setLastInvestAchievementId(Long lastInvestAchievementId) {
        this.lastInvestAchievementId = lastInvestAchievementId;
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

    public String getCreatedLoginName() {
        return createdLoginName;
    }

    public void setCreatedLoginName(String createdLoginName) {
        this.createdLoginName = createdLoginName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoanAccNo() {
        return loanAccNo;
    }

    public void setLoanAccNo(String loanAccNo) {
        this.loanAccNo = loanAccNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public void setBankOrderDate(String bankOrderDate) {
        this.bankOrderDate = bankOrderDate;
    }

    public String getLoanFullBankOrderNo() {
        return loanFullBankOrderNo;
    }

    public void setLoanFullBankOrderNo(String loanFullBankOrderNo) {
        this.loanFullBankOrderNo = loanFullBankOrderNo;
    }

    public String getLoanFullBankOrderDate() {
        return loanFullBankOrderDate;
    }

    public void setLoanFullBankOrderDate(String loanFullBankOrderDate) {
        this.loanFullBankOrderDate = loanFullBankOrderDate;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public long getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public void setExpectedRepayAmount(long expectedRepayAmount) {
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public long getActualRepayAmount() {
        return actualRepayAmount;
    }

    public void setActualRepayAmount(long actualRepayAmount) {
        this.actualRepayAmount = actualRepayAmount;
    }

    public long getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(long unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public boolean getIsBankPlatform() {
        return isBankPlatform;
    }

    public void setIsBankPlatForm(boolean isBankPlatform) {
        this.isBankPlatform = isBankPlatform;
    }

    public long getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(long loanFee) {
        this.loanFee = loanFee;
    }
}
