package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class InvestPaginationItemDataDto implements Serializable {

    private long investId;

    private long loanId;

    private String loanName;

    private String loanType;

    private int loanPeriods;

    private String amount;

    private String investorLoginName;

    private String investorUserName;

    private String investorMobile;

    private String referrerLoginName;

    private String referrerUserName;

    private String referrerMobile;

    private String referrerRoles;

    private String source;

    private String channel;

    private String roles;

    private boolean isAutoInvest;

    private String status;

    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date nextRepayDate;

    private String nextRepayAmount;

    private boolean hasInvestRepay;

    private String identityNumber;

    private String province;

    private String city;

    private boolean birthdayCoupon;

    private double birthdayBenefit;

    private String transferStatus;

    private String baseRate;

    private String activityRate;

    private String sumRate;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date lastRepayDate;

    private int leftPeriod;

    public InvestPaginationItemDataDto(InvestPaginationItemView view) {
        this.investId = view.getId();
        this.loanId = view.getLoanId();
        this.amount = AmountConverter.convertCentToString(view.getAmount());
        this.loanName = view.getLoanName();
        this.investorLoginName = view.getLoginName();
        this.investorUserName = view.getInvestorUserName();
        this.investorMobile = view.getInvestorMobile();
        this.referrerLoginName = view.getReferrerLoginName();
        this.referrerUserName = view.getReferrerUserName();
        this.referrerMobile = view.getReferrerMobile();
        this.referrerRoles = view.getReferrerRoles();
        this.source = view.getSource().name();
        this.channel = view.getChannel();
        this.roles = view.getRoles();
        this.isAutoInvest = view.isAutoInvest();
        this.loanType = view.getLoanType().getName();
        this.loanPeriods = view.getLoanPeriods();
        this.createdTime = view.getTradingTime() == null ? view.getCreatedTime() : view.getTradingTime();
        this.status = view.getStatus().getDescription();
        this.nextRepayDate = view.getNextRepayDate();
        this.nextRepayAmount = AmountConverter.convertCentToString(view.getNextRepayAmount());
        this.hasInvestRepay = view.getStatus() == InvestStatus.SUCCESS && Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE).contains(view.getLoanStatus());
        this.identityNumber = view.getIdentityNumber();
        this.province = view.getProvince();
        this.city = view.getCity();
        this.birthdayCoupon = view.isBirthdayCoupon();
        this.birthdayBenefit = view.getBirthdayBenefit();
        this.baseRate = view.getLoanBaseRatePercent();
        this.activityRate = view.getLoanActivityRatePercent();
        this.sumRate = view.getSumRatePercent();
    }

    public boolean isStaff() {
        return StringUtils.containsIgnoreCase(this.roles, Role.STAFF.name());
    }

    public boolean isReferrerStaff() {
        return StringUtils.containsIgnoreCase(this.referrerRoles, Role.STAFF.name());
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

    public String getLoanType() {
        return loanType;
    }

    public String getAmount() {
        return amount;
    }

    public String getInvestorLoginName() {
        return investorLoginName;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public String getSource() {
        return source;
    }

    public boolean isAutoInvest() {
        return isAutoInvest;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public String getNextRepayAmount() {
        return nextRepayAmount;
    }

    public boolean isHasInvestRepay() {
        return hasInvestRepay;
    }

    public int getLoanPeriods() {
        return loanPeriods;
    }

    public void setLoanPeriods(int loanPeriods) {
        this.loanPeriods = loanPeriods;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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

    public String getBirthday() {
        if (identityNumber == null) {
            return "";
        } else if (identityNumber.length() == 18) {
            return identityNumber.substring(6, 14);
        } else if (identityNumber.length() == 15) {
            return identityNumber.substring(6, 12);
        } else {
            return "";
        }
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

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
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

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public int getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(int leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getSumRate() {
        return sumRate;
    }

    public void setSumRate(String sumRate) {
        this.sumRate = sumRate;
    }
}
