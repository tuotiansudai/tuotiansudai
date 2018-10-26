package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.tuotiansudai.dto.UserCouponDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;
import java.util.List;

public class InvestorInvestPaginationItemDataDto {

    private long investId;

    private long loanId;

    private String loanName;

    private String amount;

    private Date createdTime;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextRepayDate;

    private String nextRepayAmount;

    private List<UserCouponDto> userCoupons;

    private InvestAchievement achievement;

    private boolean investRepayExist;

    private ProductType productType;

    private Double extraRate;

    private String contractNo;

    private boolean birthdayCoupon;

    private double birthdayBenefit;

    public InvestorInvestPaginationItemDataDto(LoanModel loanModel, InvestModel investModel, InvestRepayModel investRepayModel, List<UserCouponDto> userCouponDtoList, boolean investRepayExist, InvestExtraRateModel investExtraRateModel) {
        this.investId = investModel.getId();
        this.loanId = investModel.getLoanId();
        this.loanName = loanModel.getName();
        this.amount = AmountConverter.convertCentToString(investModel.getAmount());
        this.createdTime = investModel.getCreatedTime();
        this.status = investModel.getStatus().getDescription();
        this.nextRepayDate = investRepayModel != null ? investRepayModel.getRepayDate() : null;
        this.nextRepayAmount = AmountConverter.convertCentToString(investRepayModel != null ?
                investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() + investRepayModel.getOverdueInterest() - investRepayModel.getExpectedFee() - investRepayModel.getDefaultFee() - investRepayModel.getOverdueFee() : 0);
        this.userCoupons = userCouponDtoList;
        if (investModel.getAchievements() != null && investModel.getAchievements().size() > 0) {
            this.achievement =  new Ordering<InvestAchievement>() {
                @Override
                public int compare(InvestAchievement left, InvestAchievement right) {
                    return Ints.compare(left.getPriority(), right.getPriority());
                }
            }.min(investModel.getAchievements());
        }
        this.investRepayExist = investRepayExist;
        this.productType = loanModel.getProductType();
        this.extraRate = investExtraRateModel != null ? investExtraRateModel.getExtraRate() : null;
        this.contractNo = investModel.getContractNo();
    }

    public long getInvestId() {
        return investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public String getAmount() {
        return amount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getStatus() {
        return status;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public String getNextRepayAmount() {
        return nextRepayAmount;
    }

    public InvestAchievement getAchievement() {
        return achievement;
    }

    public List<UserCouponDto> getUserCoupons() {
        return userCoupons;
    }

    public boolean isInvestRepayExist() {

        return investRepayExist;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Double getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(Double extraRate) {
        this.extraRate = extraRate;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public void setNextRepayAmount(String nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public boolean getBirthdayCoupon() {
        return birthdayCoupon;
    }

    public void setBirthdayCoupon(boolean birthdayCoupon) {
        this.birthdayCoupon = birthdayCoupon;
    }

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }
}
