package com.tuotiansudai.console.bi.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestViscosityDetailView implements Serializable {

    String loginName;

    String userName;

    String mobile;

    String isStaff;

    String referrer;

    String referrerUserName;

    String isReferrerStaff;

    long totalAmount;

    Date lastInvestTime;

    long loanCount;


    public InvestViscosityDetailView() {

    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getIsReferrerStaff() {
        return isReferrerStaff;
    }

    public void setIsReferrerStaff(String isReferrerStaff) {
        this.isReferrerStaff = isReferrerStaff;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getLastInvestTime() {
        return lastInvestTime;
    }

    public void setLastInvestTime(Date lastInvestTime) {
        this.lastInvestTime = lastInvestTime;
    }

    public long getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(long loanCount) {
        this.loanCount = loanCount;
    }
}
