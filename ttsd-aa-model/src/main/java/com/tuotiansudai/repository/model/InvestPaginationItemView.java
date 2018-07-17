package com.tuotiansudai.repository.model;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.tuotiansudai.enums.Role;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvestPaginationItemView implements Serializable {
    private long investId;
    private long loanId;
    private String loanName;
    private int loanPeriods;
    private String investorLoginName;
    private String investorUserName;
    private String investorMobile;
    private String investorRoles;
    private String referrerLoginName;
    private String referrerUserName;
    private String referrerMobile;
    private String referrerRoles;
    private String channel;
    private Source source;
    private Date investTime;
    private long investAmount;
    private long couponId;
    private double extraRate;
    private long extraActualInterest;
    private InvestStatus investStatus;
    private String province;
    private String city;
    private String identityNumber;
    //新增资金渠道
    private Boolean isBankPlatform;

    public List<Role> getInvestorRoleList() {
        List<Role> roles = new ArrayList<>();
        if (!Strings.isNullOrEmpty(investorRoles) && !Strings.isNullOrEmpty(investorRoles.trim())) {
            List<String> roleStrings = Splitter.on(",").trimResults().splitToList(investorRoles);
            for (String roleString : roleStrings) {
                roles.add(Role.valueOf(roleString));
            }
        }
        return roles;
    }

    public List<Role> getReferrerRoleList() {
        List<Role> roles = new ArrayList<>();
        if (!StringUtils.isEmpty(referrerRoles) && !StringUtils.isEmpty(referrerRoles.trim())) {
            List<String> roleStrings = Splitter.on(",").trimResults().splitToList(referrerRoles);
            for (String roleString : roleStrings) {
                roles.add(Role.valueOf(roleString));
            }
        }
        return roles;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
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

    public int getLoanPeriods() {
        return loanPeriods;
    }

    public void setLoanPeriods(int loanPeriods) {
        this.loanPeriods = loanPeriods;
    }

    public String getInvestorLoginName() {
        return investorLoginName;
    }

    public void setInvestorLoginName(String investorLoginName) {
        this.investorLoginName = investorLoginName;
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

    public String getInvestorRoles() {
        return investorRoles;
    }

    public void setInvestorRoles(String investorRoles) {
        this.investorRoles = investorRoles;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public double getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(double extraRate) {
        this.extraRate = extraRate;
    }

    public long getExtraActualInterest() {
        return extraActualInterest;
    }

    public void setExtraActualInterest(long extraActualInterest) {
        this.extraActualInterest = extraActualInterest;
    }

    public InvestStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(InvestStatus investStatus) {
        this.investStatus = investStatus;
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Boolean getIsBankPlatform() {
        return isBankPlatform;
    }

    public void setIsBankPlatform(Boolean isBankPlatform) {
        this.isBankPlatform = isBankPlatform;
    }
}
