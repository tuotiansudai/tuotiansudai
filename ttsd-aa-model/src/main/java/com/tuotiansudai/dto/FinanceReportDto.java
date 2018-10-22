package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.FinanceReportItemView;
import com.tuotiansudai.util.AmountConverter;

import java.text.MessageFormat;
import java.util.Date;

public class FinanceReportDto {
    private long loanId;    //项目编号 LoanModel.id
    private String loanName;  //项目名称 LoanModel.name
    private String loanType;  //计息模式  LoanModel.LoanType
    private String loanerUserName; //借款人 LoanModel.loanerUserName
    private String agentLoginName; //他项人 LoanModel.agentLoginName
    private String benefitRate; //收益率 format:"this.baseRate+this.activityRate"
    private int duration;    //周期 LoanModel.duration
    private String loanAmount;    //标的金额  LoanModel.loanAmount
    private Date verifyTime;   //起标日期  LoanModel.verifyTime
    private Date investTime;    //出借时间  InvestModel.investTime
    private Date recheckTime; //放款时间  LoanModel.recheckTime
    private String investLoginName; //出借人 InvestModel.loginName
    private String investRealName;  //出借人姓名 Account.userName
    private String referrer;  //推荐人 UserModel.referrer
    private String investAmount;  //出借金额 InvestModel.amount
    private int benefitDays;    //计息天数 这一期InvestRepayModel.RepayDate-上一期InvestRepayModel.RepayDate 第一期天数是:即
    //投即生息-InvestRepayModel.repayDate-InvestModel.investTime,放款后生息-InvestRepayModel.repayDate-LoanModel.recheckTime
    private Date repayTime; //回款时间  InvestRepayModel.repayDate
    private int period;  //期限    InvestRepayModel.period
    private String expectInterest; //预期收益
    private String overdueInterest; //逾期收益
    private String actualInterest; //实际收益 InvestRepayModel.actualInterest
    private String overdueFee;   //逾期服务费   InvestRepayModel.overdueFee
    private String fee;   //服务费   InvestRepayModel.actualFee
    private String actualRepayAmount; //实际回款  InvestRepayModel.repayAmount
    private String recommendAmount;   //推荐奖励 InvestReferrerRewardModel.amount and InvestReferrerRewardModel.status = SUCCESS
    private String couponDetail;    //使用红包详情
    private String couponActualInterest;    //使用红包实际返款
    private String extraDetail; //使用阶梯加息详情
    private String extraActualInterest; //使用阶梯加息实际返款

    public FinanceReportDto() {
    }

    public FinanceReportDto(FinanceReportItemView financeReportItemView) {
        this.setLoanId(financeReportItemView.getLoanId());
        this.setLoanName(financeReportItemView.getLoanName());
        this.setLoanType(financeReportItemView.getLoanType().getInterestType());
        this.setLoanerUserName(financeReportItemView.getLoanerUserName());
        this.setAgentLoginName(financeReportItemView.getAgentLoginName());
        this.setBenefitRate(financeReportItemView.getBaseRate(), financeReportItemView.getActivityRate());
        this.setDuration(financeReportItemView.getDuration());
        this.setLoanAmount(AmountConverter.convertCentToString(financeReportItemView.getLoanAmount()));
        this.setVerifyTime(financeReportItemView.getVerifyTime());
        this.setInvestTime(financeReportItemView.getInvestTime());
        this.setRecheckTime(financeReportItemView.getRecheckTime());
        this.setInvestLoginName(financeReportItemView.getInvestLoginName());
        this.setInvestRealName(financeReportItemView.getInvestRealName());
        this.setReferrer(financeReportItemView.getReferrer());
        this.setInvestAmount(AmountConverter.convertCentToString(financeReportItemView.getInvestAmount()));
        this.setRepayTime(financeReportItemView.getRepayTime());
        this.setPeriod(financeReportItemView.getPeriod());
        this.setActualInterest(AmountConverter.convertCentToString(financeReportItemView.getActualInterest()));
        this.setFee(AmountConverter.convertCentToString(financeReportItemView.getFee()));
        this.setActualRepayAmount(AmountConverter.convertCentToString(financeReportItemView.getActualRepayAmount()));
        this.extraDetail = MessageFormat.format("{0,number,#.##}%", financeReportItemView.getExtraRate() * 100);
        this.extraActualInterest = AmountConverter.convertCentToString(financeReportItemView.getExtraAmount());
        this.expectInterest = AmountConverter.convertCentToString(financeReportItemView.getExpectInterest());
        this.overdueInterest = AmountConverter.convertCentToString(financeReportItemView.getOverdueInterest());
        this.overdueFee = AmountConverter.convertCentToString(financeReportItemView.getOverdueFee());
    }

    public void setCouponDetail(CouponModel couponModel) {
        switch (couponModel.getCouponType()) {
            case RED_ENVELOPE:
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                this.couponDetail = MessageFormat.format("{0}元{1}", AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
                break;
            case INTEREST_COUPON:
                this.couponDetail = MessageFormat.format("{0,number,#.##}%{1}", couponModel.getRate() * 100, couponModel.getCouponType().getName());
                break;
            case BIRTHDAY_COUPON:
                this.couponDetail = MessageFormat.format("{0}倍{1}", couponModel.getBirthdayBenefit() + 1, couponModel.getCouponType().getName());
                break;
            default:
                this.couponDetail = "";
                break;
        }
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

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public String getBenefitRate() {
        return benefitRate;
    }

    public void setBenefitRate(double baseRate, double activityRate) {
        this.benefitRate = baseRate + "%+" + activityRate + "%";
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
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

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public int getBenefitDays() {
        return benefitDays;
    }

    public void setBenefitDays(int benefitDays) {
        this.benefitDays = benefitDays;
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

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getActualRepayAmount() {
        return actualRepayAmount;
    }

    public void setActualRepayAmount(String actualRepayAmount) {
        this.actualRepayAmount = actualRepayAmount;
    }

    public String getRecommendAmount() {
        return recommendAmount;
    }

    public void setRecommendAmount(String recommendAmount) {
        this.recommendAmount = recommendAmount;
    }

    public String getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(String couponDetail) {
        this.couponDetail = couponDetail;
    }

    public String getCouponActualInterest() {
        return couponActualInterest;
    }

    public void setCouponActualInterest(long couponActualInterest) {
        this.couponActualInterest = AmountConverter.convertCentToString(couponActualInterest);
    }

    public String getExtraDetail() {
        return extraDetail;
    }

    public void setExtraDetail(String extraDetail) {
        this.extraDetail = extraDetail;
    }

    public String getExtraActualInterest() {
        return extraActualInterest;
    }

    public void setExtraActualInterest(String extraActualInterest) {
        this.extraActualInterest = extraActualInterest;
    }

    public String getExpectInterest() {
        return expectInterest;
    }

    public void setExpectInterest(String expectInterest) {
        this.expectInterest = expectInterest;
    }

    public String getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(String overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public String getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(String overdueFee) {
        this.overdueFee = overdueFee;
    }
}
