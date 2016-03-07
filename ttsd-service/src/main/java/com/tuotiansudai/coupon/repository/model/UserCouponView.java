package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserCouponView implements Serializable, Comparable<UserCouponView> {

    private static final long serialVersionUID = 6557972198633723582L;

    private long id;

    private CouponType couponType;

    private long couponAmount;

    private double rate;

    private List<ProductType> productTypeList;

    private long loanId;

    private String loanName;

    private long investAmount;

    private Date usedTime;

    private Date endTime;

    private long expectedIncome;

    private long investLowerLimit;

    private long investUpperLimit;

    private double birthdayBenefit;

    private InvestStatus status;

    public UserCouponView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public long getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(long couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(long expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(long investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public long getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(long investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public InvestStatus getStatus() {
        return status;
    }

    public void setStatus(InvestStatus status) {
        this.status = status;
    }

    private int getStatusCode() {
        if (InvestStatus.SUCCESS == this.status) return 2; // used
        else if (new DateTime(this.endTime).isBeforeNow()) return 3; // expired
        else return 1; // unused
    }

    private long getCompareBenefitValue() {
        switch (this.couponType) {
            case RED_ENVELOPE:
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                return couponAmount;
            case INTEREST_COUPON:
                return (long) (rate * 10000);
            case BIRTHDAY_COUPON:
                return (long) (birthdayBenefit * 10000);
            default:
                return couponAmount;
        }
    }

    @Override
    public int compareTo(UserCouponView dto) {
        if (this.getStatusCode() == dto.getStatusCode()) {
            if (this.getCouponType().getOrder() == dto.getCouponType().getOrder()) {
                if (this.getStatusCode() == 2) {
                    return this.usedTime.after(dto.getUsedTime()) ? -1 : 1;
                } else if (this.getStatusCode() == 1) {
                    if (this.getCompareBenefitValue() == dto.getCompareBenefitValue()) {
                        return this.getEndTime().before(dto.getEndTime()) ? 1 : -1;
                    } else {
                        return this.getCompareBenefitValue() - dto.getCompareBenefitValue() > 0 ? 1 : -1;
                    }
                } else {
                    return this.getEndTime().after(dto.getEndTime()) ? -1 : 1;
                }
            } else {
                return this.getCouponType().getOrder() - dto.getCouponType().getOrder();
            }
        } else {
            return this.getStatusCode() - dto.getStatusCode();
        }
    }
}
