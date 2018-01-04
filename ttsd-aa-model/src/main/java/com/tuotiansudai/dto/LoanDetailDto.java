package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.*;

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

    private String raisingDays;

    private List<LoanTitleModel> loanTitleDto;

    private List<LoanTitleRelationModel> loanTitles;

    private LoanInvestAchievementDto achievement;

    private InvestorDto investor;

    private Map<String, String> loanerDetail;

    private PledgeType pledgeType;

    private List<Map<String, String>> pledgeHouseDetailList;

    private List<Map<String, String>> pledgeVehicleDetailList;

    private List<Map<String, String>> pledgeEnterpriseDetailList;

    private Map<String, String> loanerEnterpriseDetailsInfo;

    private Map<String, String> enterpriseInfo;

    private String declaration;

    private String basicInfo;

    private String extraSource;

    private boolean activity;

    private String activityDesc;

    private boolean nonTransferable;

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
        this.raisingDays = String.valueOf(Days.daysBetween(new DateTime(loanModel.getFundraisingStartTime()).withTimeAtStartOfDay(),
                new DateTime(loanModel.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1);
        this.investor = investorDto;
        this.declaration = loanDetails == null ? null : loanDetails.getDeclaration();
        this.extraSource = loanDetails == null ? null : (loanDetails.getExtraSource() != null && loanDetails.getExtraSource().size() == 1 && loanDetails.getExtraSource().contains(Source.MOBILE)) ? Source.MOBILE.name() : null;
        this.activity = loanDetails != null && loanDetails.isActivity();
        this.activityDesc = loanDetails == null ? "" : loanDetails.getActivityDesc();
        this.nonTransferable = loanDetails != null && loanDetails.getNonTransferable();
        this.pledgeType = loanModel.getPledgeType();
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

    public String getRaisingDays() {
        return raisingDays;
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

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public List<Map<String, String>> getPledgeHouseDetailList() {
        return pledgeHouseDetailList;
    }

    public void setPledgeHouseDetailList(List<Map<String, String>> pledgeHouseDetailList) {
        this.pledgeHouseDetailList = pledgeHouseDetailList;
    }

    public List<Map<String, String>> getPledgeVehicleDetailList() {
        return pledgeVehicleDetailList;
    }

    public void setPledgeVehicleDetailList(List<Map<String, String>> pledgeVehicleDetailList) {
        this.pledgeVehicleDetailList = pledgeVehicleDetailList;
    }

    public List<Map<String, String>> getPledgeEnterpriseDetailList() {
        return pledgeEnterpriseDetailList;
    }

    public void setPledgeEnterpriseDetailList(List<Map<String, String>> pledgeEnterpriseDetailList) {
        this.pledgeEnterpriseDetailList = pledgeEnterpriseDetailList;
    }

    public Map<String, String> getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public Map<String, String> getLoanerEnterpriseDetailsInfo() {
        return loanerEnterpriseDetailsInfo;
    }

    public void setLoanerEnterpriseDetailsInfo(Map<String, String> loanerEnterpriseDetailsInfo) {
        this.loanerEnterpriseDetailsInfo = loanerEnterpriseDetailsInfo;
    }

    public void setEnterpriseInfo(Map<String, String> enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    public String getDeclaration() {
        return declaration;
    }

    public String getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(String extraSource) {
        this.extraSource = extraSource;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public boolean isNonTransferable() {
        return nonTransferable;
    }

    public void setNonTransferable(boolean nonTransferable) {
        this.nonTransferable = nonTransferable;
    }
}
