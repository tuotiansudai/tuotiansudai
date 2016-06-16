package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserInvestRepayResponseDataDto extends BaseResponseDataDto {

    private String loanId;
    private String loanName;
    private String baseRate;
    private String activityRate;
    private String duration;
    private String interestInitiateType;
    private String productNewType;
    private String investId;
    private String investAmount;
    private String expectedInterest;
    private String actualInterest;
    private String investTime;
    private String recheckTime;
    private String lastRepayDate;
    private List<InvestRepayDataDto> investRepays;


    public UserInvestRepayResponseDataDto(LoanModel loanModel, InvestModel investModel){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.loanId = String.valueOf(loanModel.getId());
        this.loanName = loanModel.getName();
        this.baseRate = String.valueOf(decimalFormat.format(loanModel.getBaseRate() * 100));
        this.activityRate = String.valueOf(decimalFormat.format(loanModel.getActivityRate() * 100));
        this.duration = String.valueOf(loanModel.getDuration());
        this.interestInitiateType = loanModel.getType().getInterestInitiateType().name();
        this.productNewType = loanModel.getProductType().name();
        this.recheckTime = String.valueOf(loanModel.getRecheckTime()==null?"":sdf2.format(loanModel.getRecheckTime()));
        this.investId = String.valueOf(investModel.getId());
        this.investAmount = AmountConverter.convertCentToString(investModel.getAmount());
        this.investTime = sdf.format(investModel.getInvestTime());
    }


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

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInterestInitiateType() {
        return interestInitiateType;
    }

    public void setInterestInitiateType(String interestInitiateType) {
        this.interestInitiateType = interestInitiateType;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
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

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(String recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public List<InvestRepayDataDto> getInvestRepays() {
        return investRepays;
    }

    public void setInvestRepays(List<InvestRepayDataDto> investRepays) {
        this.investRepays = investRepays;
    }
}
