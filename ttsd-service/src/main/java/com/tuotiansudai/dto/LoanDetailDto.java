package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LoanDetailDto extends BaseDataDto {

    private long id;

    private String name;

    private ProductType productType;

    private LoanType type;

    private ActivityType activityType;

    private int periods;

    private int duration;

    private long loanAmount;

    private double baseRate;

    private double activityRate;

    private String minInvestAmount;

    private String maxInvestAmount;

    private double progress;

    private Date fundraisingStartTime;

    private LoanStatus loanStatus;

    private double newbieInterestCouponRate;

    private long amountNeedRaised;

    private long countdown;

    private Period raisingPeriod;

    private List<LoanTitleModel> loanTitleDto;

    private List<LoanTitleRelationModel> loanTitles;

    private LoanInvestAchievementDto achievement;

    private InvestorDto investor;

    private Map<String, String> loanerDetail;

    private Map<String, String> pledgeHouseDetail;

    private Map<String, String> pledgeVehicleDetail;

    private String declaration;

    public LoanDetailDto(LoanModel loanModel, LoanDetailsModel loanDetails, long investedAmount, List<LoanTitleModel> loanTitleModels, List<LoanTitleRelationModel> loanTitleRelationModels, InvestorDto investorDto) {
        this.id = loanModel.getId();
        this.name = loanModel.getName();
        this.progress = new BigDecimal(investedAmount).divide(new BigDecimal(loanModel.getLoanAmount()), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
        this.baseRate = new BigDecimal(loanModel.getBaseRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.activityRate = new BigDecimal(loanModel.getActivityRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.loanAmount = loanModel.getLoanAmount();
        this.periods = loanModel.getPeriods();
        this.type = loanModel.getType();
        this.minInvestAmount = AmountConverter.convertCentToString(loanModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(loanModel.getMaxInvestAmount());
        this.productType = loanModel.getProductType();
        this.activityType = loanModel.getActivityType();
        this.loanStatus = loanModel.getStatus();
        this.amountNeedRaised = loanModel.getLoanAmount() - investedAmount;
        this.fundraisingStartTime = loanModel.getFundraisingStartTime();
        this.duration = loanModel.getDuration();
        this.loanTitles = loanTitleRelationModels;
        this.loanTitleDto = loanTitleModels;
        this.countdown = Seconds.secondsBetween(new DateTime(), new DateTime(loanModel.getFundraisingStartTime())).getSeconds();
        this.raisingPeriod = new Period(new DateTime(), new DateTime(loanModel.getFundraisingEndTime()), PeriodType.dayTime());
        this.investor = investorDto;
        this.declaration = loanDetails.getDeclaration();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public LoanType getType() {
        return type;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public int getPeriods() {
        return periods;
    }

    public int getDuration() {
        return duration;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public double getProgress() {
        return progress;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public double getNewbieInterestCouponRate() {
        return newbieInterestCouponRate;
    }

    public long getAmountNeedRaised() {
        return amountNeedRaised;
    }

    public long getCountdown() {
        return countdown;
    }

    public Period getRaisingPeriod() {
        return raisingPeriod;
    }

    public List<LoanTitleModel> getLoanTitleDto() {
        return loanTitleDto;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public LoanInvestAchievementDto getAchievement() {
        return achievement;
    }

    public InvestorDto getInvestor() {
        return investor;
    }

    public void setNewbieInterestCouponRate(double newbieInterestCouponRate) {
        this.newbieInterestCouponRate = newbieInterestCouponRate;
    }

    public void setAchievement(LoanInvestAchievementDto achievement) {
        this.achievement = achievement;
    }

    public Map<String, String> getLoanerDetail() {
        return loanerDetail;
    }

    public void setLoanerDetail(Map<String, String> loanerDetail) {
        this.loanerDetail = loanerDetail;
    }

    public void setPledgeHouseDetail(Map<String, String> pledgeHouseDetail) {
        this.pledgeHouseDetail = pledgeHouseDetail;
    }

    public Map getPledgeHouseDetail() {
        return pledgeHouseDetail;
    }

    public void setPledgeVehicleDetail(Map<String, String> pledgeVehicleDetail) {
        this.pledgeVehicleDetail = pledgeVehicleDetail;
    }

    public Map<String, String> getPledgeVehicleDetail() {
        return pledgeVehicleDetail;
    }

    public String getDeclaration() {
        return declaration;
    }
}
