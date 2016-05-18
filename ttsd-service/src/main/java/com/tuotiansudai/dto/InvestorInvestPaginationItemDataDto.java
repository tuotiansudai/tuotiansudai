package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
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

    private List<InvestAchievement> achievements;

    private boolean investRepayExist;

    public InvestorInvestPaginationItemDataDto(String loanName, InvestModel investModel, InvestRepayModel investRepayModel, List<UserCouponDto> userCouponDtoList, boolean investRepayExist) {
        this.investId = investModel.getId();
        this.loanId = investModel.getLoanId();
        this.loanName = loanName;
        this.amount = AmountConverter.convertCentToString(investModel.getAmount());
        this.createdTime = investModel.getCreatedTime();
        this.status = investModel.getStatus().getDescription();
        this.nextRepayDate = investRepayModel != null ? investRepayModel.getRepayDate() : null;
        this.nextRepayAmount = AmountConverter.convertCentToString(investRepayModel != null ?
                investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee() : 0);
        this.userCoupons = userCouponDtoList;
        this.achievements = investModel.getAchievements();
        this.investRepayExist = investRepayExist;
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

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public List<UserCouponDto> getUserCoupons() {
        return userCoupons;
    }

    public boolean isInvestRepayExist() {

        return investRepayExist;
    }
}
