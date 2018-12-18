package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class FinanceReportItemView implements Serializable {
    private long loanId;    //项目编号 LoanModel.id
    private String loanName;  //项目名称 LoanModel.name
    private LoanType loanType;  //计息模式  LoanModel.LoanType
    private String loanerUserName; //借款人 LoanModel.loanerUserName
    private String agentLoginName; //他项人 LoanModel.agentLoginName
    private double baseRate;    //收益率-基本利率 LoanModel.baseRate
    private double activityRate;    //收益率-活动利率 LoanModel.activityRate
    private int duration;    //周期 LoanModel.duration
    private long loanAmount;    //标的金额  LoanModel.loanAmount
    private Date verifyTime;   //起标日期  LoanModel.verifyTime
    private Date investTime;    //出借时间  InvestModel.investTime
    private Date recheckTime; //放款时间  LoanModel.recheckTime
    private long investId;  //出借Id  InvestModel.id
    private String investLoginName; //出借人 InvestModel.loginName
    private String investRealName;  //出借人姓名 Account.loginName
    private String referrer;  //推荐人 UserModel.referrer
    private long investAmount;  //出借金额 InvestModel.amount
    private Date repayTime; //回款时间  InvestRepayModel.repayDate
    private int period;  //期限    InvestRepayModel.period
    private long expectInterest;  //预期期限
    private long overdueInterest;  //逾期期限
    private long actualInterest; //实际收益 InvestRepayModel.actualInterest
    private long overdueFee;   //逾期服务费   InvestRepayModel.overdueFee
    private long fee;   //服务费   InvestRepayModel.actualFee
    private long actualRepayAmount; //实际回款  InvestRepayModel.repayAmount
    private long couponId;  //使用红包
    private double extraRate; //使用阶梯加息利率
    private long extraAmount; //阶梯加息加息金额

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

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerLoginName) {
        this.loanerUserName = loanerLoginName;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
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

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
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

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public String getInvestLoginName() {
        return investLoginName;
    }

    public void setInvestLoginName(String investLoginName) {
        this.investLoginName = investLoginName;
    }

    public String getInvestRealName() {
        return investRealName;
    }

    public void setInvestRealName(String investRealName) {
        this.investRealName = investRealName;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getActualRepayAmount() {
        return actualRepayAmount;
    }

    public void setActualRepayAmount(long actualRepayAmount) {
        this.actualRepayAmount = actualRepayAmount;
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

    public long getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(long extraAmount) {
        this.extraAmount = extraAmount;
    }

    public long getExpectInterest() {
        return expectInterest;
    }

    public void setExpectInterest(long expectInterest) {
        this.expectInterest = expectInterest;
    }

    public long getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(long overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public long getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(long overdueFee) {
        this.overdueFee = overdueFee;
    }
}
