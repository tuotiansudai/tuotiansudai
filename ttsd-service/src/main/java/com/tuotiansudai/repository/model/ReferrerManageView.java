package com.tuotiansudai.repository.model;


import java.util.Date;

public class ReferrerManageView {

    private String loanId;

    private String loanName;

    private int periods;

    private String investLoginName;

    private String investName;

    private long investAmount;

    private String investAmountStr;

    private Date investTime;

    private String referrerLoginName;

    private String referrerName;

    private Role role;

    private Source source;

    private int level;

    private long rewardAmount;

    private String rewardAmountStr;

    private ReferrerRewardStatus status;

    private Date rewardTime;

    private ProductType productType;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public String getInvestLoginName() {
        return investLoginName;
    }

    public void setInvestLoginName(String investLoginName) {
        this.investLoginName = investLoginName;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(long rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferrerRewardStatus getStatus() {
        return status;
    }

    public void setStatus(ReferrerRewardStatus status) {
        this.status = status;
    }

    public Date getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(Date rewardTime) {
        this.rewardTime = rewardTime;
    }

    public String getInvestAmountStr() {
        return investAmountStr;
    }

    public void setInvestAmountStr(String investAmountStr) {
        this.investAmountStr = investAmountStr;
    }

    public String getRewardAmountStr() {
        return rewardAmountStr;
    }

    public void setRewardAmountStr(String rewardAmountStr) {
        this.rewardAmountStr = rewardAmountStr;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
