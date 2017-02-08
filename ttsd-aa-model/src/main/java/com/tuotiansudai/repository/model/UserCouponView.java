package com.tuotiansudai.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.enums.CouponType;
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

    private double birthdayBenefit;

    private InvestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    private boolean shared;

    private ProductType loanProductType;

    private UserGroup userGroup;

    private String couponSource;

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

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public ProductType getLoanProductType() {
        return loanProductType;
    }

    public void setLoanProductType(ProductType loanProductType) {
        this.loanProductType = loanProductType;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public InvestStatus getStatus() {
        return status;
    }

    public void setStatus(InvestStatus status) {
        this.status = status;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource;
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
                    return dto.usedTime.compareTo(this.getUsedTime());
                } else if (this.getStatusCode() == 1) {
                    if (this.getCompareBenefitValue() == dto.getCompareBenefitValue()) {
                        return dto.getEndTime().compareTo(this.getEndTime());
                    } else {
                        return this.getCompareBenefitValue() - dto.getCompareBenefitValue() > 0 ? 1 : -1;
                    }
                } else {
                    return dto.getEndTime().compareTo(this.getEndTime());
                }
            } else {
                return this.getCouponType().getOrder() - dto.getCouponType().getOrder();
            }
        } else {
            return this.getStatusCode() - dto.getStatusCode();
        }
    }
}
