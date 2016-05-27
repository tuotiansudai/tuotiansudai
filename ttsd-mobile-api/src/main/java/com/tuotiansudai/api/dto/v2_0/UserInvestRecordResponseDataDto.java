package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestStatus;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.util.List;

public class UserInvestRecordResponseDataDto extends BaseResponseDataDto{

    private String loanId;

    private String loanName;

    private String investId;

    private String investAmount;

    private String investTime;

    private InvestStatus investStatus;

    private String investStatusDesc;

    private String expectedInterest;

    private String actualInterest;

    private String lastRepayDate;

    private String transferStatus;

    private List<InvestAchievement> achievements;

    private List<CouponType> userCoupons;

    private boolean usedCoupon;

    private boolean usedRedEnvelope;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public InvestStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(InvestStatus investStatus) {
        this.investStatus = investStatus;
    }

    public String getInvestStatusDesc() {
        return investStatusDesc;
    }

    public void setInvestStatusDesc(String investStatusDesc) {
        this.investStatusDesc = investStatusDesc;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<InvestAchievement> achievements) {
        this.achievements = achievements;
    }

    public List<CouponType> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<CouponType> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public boolean isUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(boolean usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public boolean isUsedRedEnvelope() {
        return usedRedEnvelope;
    }

    public void setUsedRedEnvelope(boolean usedRedEnvelope) {
        this.usedRedEnvelope = usedRedEnvelope;
    }

    public UserInvestRecordResponseDataDto() {

    }

    public UserInvestRecordResponseDataDto(InvestModel invest, LoanModel loan) {
        InvestStatus investStatus = InvestStatus.convertInvestStatus(invest.getStatus());
        this.loanId = String.valueOf(invest.getLoanId());
        this.loanName = loan.getName();
        this.investId = String.valueOf(invest.getId());
        this.investAmount = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = new DateTime(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime()).toString("yyyy-MM-dd");
        this.investStatus = investStatus;
        this.investStatusDesc = investStatus.getMessage();
        this.achievements = invest.getAchievements();
    }

}
