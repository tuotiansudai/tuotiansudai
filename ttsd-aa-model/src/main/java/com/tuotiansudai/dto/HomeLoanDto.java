package com.tuotiansudai.dto;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class HomeLoanDto {

    private long id;

    private String name;

    private ProductType productType;

    private ActivityType activityType;

    private double baseRate;

    private double activityRate;

    private int periods;

    private String amount;

    private double progress;

    private String status;

    private Date fundraisingStartTime;

    private long preheatSeconds;

    private double newbieInterestCouponRate;

    private String availableInvestAmount;

    private long availableInvestAmountCent;

    private int completedPeriods;

    private int duration;

    private double extraRate;

    private List<Source> extraSource;

    private boolean activity;

    private String activityDesc;

    public HomeLoanDto(CouponModel newbieInterestCouponModel, LoanModel loan, long investAmount, List<LoanRepayModel> loanRepayModels, double extraRate, List<Source> extraSource, boolean activity, String activityDesc) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.productType = loan.getProductType();
        this.activityType = loan.getActivityType();
        this.baseRate = new BigDecimal(String.valueOf(loan.getBaseRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        if (loan.getActivityRate() > 0) {
            this.activityRate = new BigDecimal(String.valueOf(loan.getActivityRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        this.periods = loan.getPeriods();
        this.duration = loan.getDuration();
        this.amount = new DecimalFormat("#.00").format(loan.getLoanAmount());
        this.progress = Lists.newArrayList(LoanStatus.RECHECK, LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE).contains(loan.getStatus()) ? 100 : new BigDecimal(investAmount).divide(new BigDecimal(loan.getLoanAmount()), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
        this.status = loan.getStatus().name();
        this.fundraisingStartTime = loan.getFundraisingStartTime();
        this.preheatSeconds = (loan.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000;
        if (newbieInterestCouponModel != null && newbieInterestCouponModel.getProductTypes().contains(loan.getProductType())) {
            this.newbieInterestCouponRate = new BigDecimal(String.valueOf(newbieInterestCouponModel.getRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        this.availableInvestAmountCent = Lists.newArrayList(LoanStatus.RECHECK, LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE).contains(loan.getStatus()) ? 0 : loan.getLoanAmount() - investAmount;
        this.availableInvestAmount = AmountConverter.convertCentToString(loan.getLoanAmount() - investAmount);
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() == RepayStatus.COMPLETE) {
                completedPeriods++;
            }
        }
        if (extraRate != 0) {
            this.extraRate = extraRate;
            this.extraSource = extraSource;
        }
        this.activity = activity;
        this.activityDesc = activityDesc;
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

    public ActivityType getActivityType() {
        return activityType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public int getPeriods() {
        return periods;
    }

    public String getAmount() {
        return amount;
    }

    public double getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public double getNewbieInterestCouponRate() {
        return newbieInterestCouponRate;
    }

    public String getAvailableInvestAmount() {
        return availableInvestAmount;
    }

    public long getAvailableInvestAmountCent() {
        return availableInvestAmountCent;
    }

    public int getCompletedPeriods() {
        return completedPeriods;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(double extraRate) {
        this.extraRate = extraRate;
    }

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public boolean getActivity() {
        return activity;
    }
}
