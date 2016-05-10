package com.tuotiansudai.repository.model;

import java.util.Date;
import java.util.List;

public class InvestPaginationItemView extends InvestModel {

    private String loanName;

    private LoanStatus loanStatus;

    private LoanType loanType;

    private int loanPeriods;

    private String roles;

    private String investorUserName;

    private String investorMobile;

    private String referrerLoginName;

    private String referrerUserName;

    private String referrerMobile;

    private String referrerRoles;

    private Date nextRepayDate;

    private long nextRepayAmount;

    private String identityNumber;

    private String province;

    private String city;

    private boolean birthdayCoupon;

    private double birthdayBenefit;

    private List<Integer> couponTypeList;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getLoanPeriods() {
        return loanPeriods;
    }

    public void setLoanPeriods(int loanPeriods) {
        this.loanPeriods = loanPeriods;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public long getNextRepayAmount() {
        return nextRepayAmount;
    }

    public void setNextRepayAmount(long nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public String getInvestorUserName() {
        return investorUserName;
    }

    public void setInvestorUserName(String investorUserName) {
        this.investorUserName = investorUserName;
    }

    public String getInvestorMobile() {
        return investorMobile;
    }

    public void setInvestorMobile(String investorMobile) {
        this.investorMobile = investorMobile;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public String getReferrerRoles() {
        return referrerRoles;
    }

    public void setReferrerRoles(String referrerRoles) {
        this.referrerRoles = referrerRoles;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isBirthdayCoupon() {
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

    public List<Integer> getCouponTypeList() { return couponTypeList; }

    public void setCouponTypeList(List<Integer> couponTypeList) { this.couponTypeList = couponTypeList; }
}
