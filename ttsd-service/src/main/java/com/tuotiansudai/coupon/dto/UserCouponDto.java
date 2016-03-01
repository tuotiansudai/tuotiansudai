package com.tuotiansudai.coupon.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserCouponDto implements Serializable, Comparable<UserCouponDto> {
    private long id;
    private long couponId;
    private String loginName;
    private CouponType couponType;
    private String name;
    private long amount;
    private double rate;
    private double birthdayBenefit;
    private boolean multiple;
    private Date startTime;
    private Date endTime;
    private Date usedTime;
    private Long loanId;
    private boolean used;
    private boolean expired;
    private boolean unused;
    private boolean shared;
    private long investLowerLimit;
    private long investUpperLimit;
    private Date createdTime;
    private List<ProductType> productTypeList;

    public UserCouponDto() {
    }

    public UserCouponDto(CouponModel coupon, UserCouponModel userCoupon) {
        this.id = userCoupon.getId();
        this.couponId = coupon.getId();
        this.loginName = userCoupon.getLoginName();
        this.couponType = coupon.getCouponType();
        this.name = coupon.getCouponType().getName();
        this.amount = coupon.getAmount();
        this.rate = coupon.getRate();
        this.birthdayBenefit = coupon.getBirthdayBenefit();
        this.multiple = coupon.isMultiple();
        this.startTime = userCoupon.getStartTime();
        this.endTime = userCoupon.getEndTime();
        this.usedTime = userCoupon.getUsedTime();
        this.loanId = userCoupon.getLoanId();
        this.used = InvestStatus.SUCCESS == userCoupon.getStatus();
        this.expired = !this.used && this.endTime.before(new Date());
        this.unused = !this.used && this.endTime.after(new Date());
        this.shared = coupon.isShared();
        this.investLowerLimit = coupon.getInvestLowerLimit();
        this.investUpperLimit = coupon.getInvestUpperLimit();
        this.createdTime = userCoupon.getCreatedTime();
        this.productTypeList = coupon.getProductTypes();
    }

    public long getId() {
        return id;
    }

    public long getCouponId() {
        return couponId;
    }

    public String getLoginName() {
        return loginName;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public double getRate() {
        return rate;
    }

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public Long getLoanId() {
        return loanId;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isUnused() {
        return unused;
    }

    public boolean isShared() {
        return shared;
    }

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public long getInvestUpperLimit() {
        return investUpperLimit;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    private int getStatusCode() {
        if (this.expired) return 3;
        else if (this.used) return 2;
        else return 1;
    }

    private long getCompareBenefitValue() {
        switch (this.couponType) {
            case RED_ENVELOPE:
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                return amount;
            case INTEREST_COUPON:
                return (long) (rate * 100);
            case BIRTHDAY_COUPON:
                return (long) (birthdayBenefit * 100);
            default:
                return amount;
        }
    }

    @Override
    public int compareTo(UserCouponDto dto) {
        if (this.getStatusCode() == dto.getStatusCode()) {
            if (this.getCouponType().getOrder() == dto.getCouponType().getOrder()) {
                if (this.getStatusCode() == 2) {
                    return this.usedTime.after(dto.getUsedTime()) ? -1 : 1;
                } else if (this.getStatusCode() == 1) {
                    if (this.getCompareBenefitValue() == dto.getCompareBenefitValue()) {
                        return this.getEndTime().after(dto.getEndTime()) ? 1 : -1;
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
