package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class UserInvestRecordDataDto implements Serializable {

    private String loanId;

    private String loanName;

    private String investId;

    private String transferApplicationId;

    private String investAmount;

    private String investTime;

    private InvestStatus investStatus;

    private String expectedInterest;

    private String actualInterest;

    private String lastRepayDate;

    private String transferStatus;

    private List<InvestAchievement> achievements;

    private boolean usedCoupon;

    private boolean usedRedEnvelope;

    private String productNewType;

    private String extraRate;

    private PledgeType pledgeType;

    private boolean isTransferInvest;

    private int repayProgress;

    private boolean isBankPlatform;

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

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(String extraRate) {
        this.extraRate = extraRate;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public boolean isTransferInvest() {
        return isTransferInvest;
    }

    public void setTransferInvest(boolean transferInvest) {
        isTransferInvest = transferInvest;
    }

    public int getRepayProgress() {
        return repayProgress;
    }

    public void setRepayProgress(int repayProgress) {
        this.repayProgress = repayProgress;
    }

    public boolean isBankPlatform() {
        return isBankPlatform;
    }

    public void setBankPlatform(boolean bankPlatform) {
        isBankPlatform = bankPlatform;
    }

    public UserInvestRecordDataDto() {

    }

    public UserInvestRecordDataDto(InvestModel invest, LoanModel loanModel) {
        this.loanId = String.valueOf(invest.getLoanId());
        this.loanName = loanModel.getName();
        this.isBankPlatform = loanModel.getIsBankPlatform();
        this.investId = String.valueOf(invest.getId());
        this.investAmount = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = new DateTime(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime()).toString("yyyy-MM-dd");
        this.investStatus = invest.getStatus();
        this.achievements = invest.getAchievements();
        this.pledgeType = loanModel.getPledgeType();
        this.transferStatus = invest.getTransferStatus().name();
    }


}
