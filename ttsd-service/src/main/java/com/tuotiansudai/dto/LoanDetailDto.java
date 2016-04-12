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

    private String loanerLoginName;

    private String loanerUserName;

    private String agentLoginName;

    private LoanType type;

    private int periods;

    private String descriptionHtml;

    private long minInvestAmount;

    private long investIncreasingAmount;

    private String maxInvestAmount;

    private ProductType productType;

    private double basicRate;

    private double activityRate;

    private Date fundraisingStartTime;

    private long loanAmount;

    private Date recheckTime;

    private LoanStatus loanStatus;

    private List<LoanTitleModel> loanTitleDto;

    private long amountNeedRaised;

    private String maxAvailableInvestAmount;

    private double progress;

    private long preheatSeconds;

    private long userBalance;

    private List<LoanTitleRelationModel> loanTitles;

    private boolean autoInvest;

    private boolean investNoPassword;

    private boolean hasRemindInvestNoPassword;

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

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
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

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
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

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public double getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(double basicRate) {
        this.basicRate = basicRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(Date fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
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

    public List<LoanTitleModel> getLoanTitleDto() {
        return loanTitleDto;
    }

    public void setLoanTitleDto(List<LoanTitleModel> loanTitleDto) {
        this.loanTitleDto = loanTitleDto;
    }

    public long getAmountNeedRaised() {
        return amountNeedRaised;
    }

    public void setAmountNeedRaised(long amountNeedRaised) {
        this.amountNeedRaised = amountNeedRaised;
    }

    public String getMaxAvailableInvestAmount() {
        return maxAvailableInvestAmount;
    }

    public void setMaxAvailableInvestAmount(String maxAvailableInvestAmount) {
        this.maxAvailableInvestAmount = maxAvailableInvestAmount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }

    public void setUserBalance(long userBalance) {
        this.userBalance = userBalance;
    }

    public long getUserBalance() {
        return userBalance;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public boolean isInvestNoPassword() {
        return investNoPassword;
    }

    public void setInvestNoPassword(boolean investNoPassword) {
        this.investNoPassword = investNoPassword;
    }

    public boolean isHasRemindInvestNoPassword() {
        return hasRemindInvestNoPassword;
    }

    public void setHasRemindInvestNoPassword(boolean hasRemindInvestNoPassword) {
        this.hasRemindInvestNoPassword = hasRemindInvestNoPassword;
    }
}
