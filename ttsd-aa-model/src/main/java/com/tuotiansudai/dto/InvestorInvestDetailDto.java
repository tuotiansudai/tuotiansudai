package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvestorInvestDetailDto implements Serializable {

    //标的ID
    private long loanId;

    //标的名称
    private String loanName;

    //基本利率
    private double baseRate;

    //活动利率
    private double activityRate;

    //借款天数
    private int duration;

    //生息类别
    private InterestInitiateType interestInitiateType;

    //标的类型
    private ProductType productNewType;

    //投资ID
    private long investId;

    //投资金额
    private long investAmount;

    //预计收益
    private long expectedInterest;

    //已收收益
    private long actualInterest;

    //投资时间
    private Date investTime;

    //放款时间
    private Date recheckTime;

    //起息日
    private Date interestBeginDate;

    //到期日
    private Date lastRepayDate;

    //待收回款
    private long unPaidRepay;

    //会员等级
    private int membershipLevel;

    //服务费折扣描述
    private String serviceFeeDesc;

    //所用优惠券
    private List<String> usedCoupons = new ArrayList<>();

    //回款记录
    private List<InvestorInvestRepayDto> investRepays = new ArrayList<>();

    //合同地址
    private String contractUrl;

    public InvestorInvestDetailDto(LoanModel loanModel, TransferApplicationModel transferApplicationModel) {
        this.loanId = transferApplicationModel.getLoanId();
        this.loanName = transferApplicationModel.getName();
        this.baseRate = loanModel.getBaseRate();
        this.activityRate = loanModel.getActivityRate();
        this.duration = loanModel.getDuration();
        this.interestInitiateType = loanModel.getType().getInterestInitiateType();
        this.productNewType = loanModel.getProductType();
        this.investId = transferApplicationModel.getInvestId();
        this.investAmount = transferApplicationModel.getInvestAmount();
        this.investTime = transferApplicationModel.getTransferTime();
        this.recheckTime = loanModel.getRecheckTime();
        this.interestBeginDate = loanModel.getRecheckTime();
    }

    public InvestorInvestDetailDto(LoanModel loanModel, InvestModel investModel) {
        this.loanId = loanModel.getId();
        this.loanName = loanModel.getName();
        this.baseRate = loanModel.getBaseRate();
        this.activityRate = loanModel.getActivityRate();
        this.duration = loanModel.getDuration();
        this.interestInitiateType = loanModel.getType().getInterestInitiateType();
        this.productNewType = loanModel.getProductType();
        this.investId = investModel.getId();
        this.investAmount = investModel.getAmount();
        this.investTime = investModel.getInvestTime();
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public InterestInitiateType getInterestInitiateType() {
        return interestInitiateType;
    }

    public void setInterestInitiateType(InterestInitiateType interestInitiateType) {
        this.interestInitiateType = interestInitiateType;
    }

    public ProductType getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(ProductType productNewType) {
        this.productNewType = productNewType;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public Date getInterestBeginDate() {
        return interestBeginDate;
    }

    public void setInterestBeginDate(Date interestBeginDate) {
        this.interestBeginDate = interestBeginDate;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public long getUnPaidRepay() {
        return unPaidRepay;
    }

    public void setUnPaidRepay(long unPaidRepay) {
        this.unPaidRepay = unPaidRepay;
    }

    public int getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public String getServiceFeeDesc() {
        return serviceFeeDesc;
    }

    public void setServiceFeeDesc(String serviceFeeDesc) {
        this.serviceFeeDesc = serviceFeeDesc;
    }

    public List<String> getUsedCoupons() {
        return usedCoupons;
    }

    public void setUsedCoupons(List<String> usedCoupons) {
        this.usedCoupons = usedCoupons;
    }

    public List<InvestorInvestRepayDto> getInvestRepays() {
        return investRepays;
    }

    public void setInvestRepays(List<InvestorInvestRepayDto> investRepays) {
        this.investRepays = investRepays;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
