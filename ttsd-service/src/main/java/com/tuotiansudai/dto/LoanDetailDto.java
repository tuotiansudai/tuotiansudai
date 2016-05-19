package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LoanDetailDto extends BaseDataDto {

    private long id;

    private String name;

    private String loanerLoginName;

    private String agentLoginName;

    private LoanType type;

    private int periods;

    private String descriptionHtml;

    private String minInvestAmount;

    private String investIncreasingAmount;

    private String maxInvestAmount;

    private ProductType productType;

    private ActivityType activityType;

    private double newbieInterestCouponRate;

    private double basicRate;

    private double activityRate;

    private Date fundraisingStartTime;

    private long loanAmount;

    private Date recheckTime;

    private LoanStatus loanStatus;

    private List<LoanTitleModel> loanTitleDto;

    private long amountNeedRaised;

    private String maxAvailableInvestAmount = "0";

    private double progress;

    private long preheatSeconds;

    private long userBalance;

    private List<LoanTitleRelationModel> loanTitles;

    private boolean autoInvest;

    private boolean investNoPassword;

    private boolean hasRemindInvestNoPassword;

    private long raisingPeriod;

    private LoanInvestAchievementDto achievement;

    public LoanDetailDto(LoanModel loanModel, long investedAmount, List<LoanTitleModel> loanTitleModels, List<LoanTitleRelationModel> loanTitleRelationModels) {
        this.id = loanModel.getId();
        this.name = loanModel.getName();
        this.progress = new BigDecimal(investedAmount).divide(new BigDecimal(loanModel.getLoanAmount()), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
        this.basicRate = new BigDecimal(loanModel.getBaseRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.activityRate = new BigDecimal(loanModel.getActivityRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.loanAmount = loanModel.getLoanAmount();
        this.agentLoginName = loanModel.getAgentLoginName();
        this.loanerLoginName = loanModel.getLoanerLoginName();
        this.periods = loanModel.getPeriods();
        this.type = loanModel.getType();
        this.minInvestAmount = AmountConverter.convertCentToString(loanModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(loanModel.getMaxInvestAmount());
        this.investIncreasingAmount = AmountConverter.convertCentToString(loanModel.getInvestIncreasingAmount());
        this.productType = loanModel.getProductType();
        this.activityType = loanModel.getActivityType();
        this.loanStatus = loanModel.getStatus();
        this.amountNeedRaised = loanModel.getLoanAmount() - investedAmount;

        this.descriptionHtml = loanModel.getDescriptionHtml();
        this.fundraisingStartTime = loanModel.getFundraisingStartTime();
        this.raisingPeriod = Days.daysBetween(new DateTime(loanModel.getFundraisingStartTime()).withTimeAtStartOfDay(), new DateTime(loanModel.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1;

        this.loanTitleDto = loanTitleModels;
        this.loanTitles = loanTitleRelationModels;

        if (loanModel.getStatus() == LoanStatus.PREHEAT) {
            this.preheatSeconds = (loanModel.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000;
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLoanerLoginName() {
        return loanerLoginName;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public LoanType getType() {
        return type;
    }

    public int getPeriods() {
        return periods;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public String getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public double getNewbieInterestCouponRate() {
        return newbieInterestCouponRate;
    }

    public void setNewbieInterestCouponRate(double newbieInterestCouponRate) {
        this.newbieInterestCouponRate = newbieInterestCouponRate;
    }

    public double getBasicRate() {
        return basicRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public List<LoanTitleModel> getLoanTitleDto() {
        return loanTitleDto;
    }

    public long getAmountNeedRaised() {
        return amountNeedRaised;
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

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setUserBalance(long userBalance) {
        this.userBalance = userBalance;
    }

    public long getUserBalance() {
        return userBalance;
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

    public long getRaisingPeriod() {
        return raisingPeriod;
    }

    public LoanInvestAchievementDto getAchievement() {
        return achievement;
    }

    public void setAchievement(LoanInvestAchievementDto achievement) {
        this.achievement = achievement;
    }
}
