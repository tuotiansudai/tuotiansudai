package com.tuotiansudai.coupon.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.InvestStatus;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class UserCouponDto implements Serializable {
    private long id;
    private long couponId;
    private Long loanId;
    private String name;
    private long amount;
    private Date startTime;
    private Date endTime;
    private boolean used;
    private boolean expired;
    private boolean unused;
    private boolean usable;
    private long investLowerLimit;

    public UserCouponDto() {
    }

    public UserCouponDto(CouponModel coupon, UserCouponModel userCoupon) {
        this.id = userCoupon.getId();
        this.couponId = coupon.getId();
        this.amount = coupon.getAmount();
        this.startTime = coupon.getStartTime();
        this.endTime = coupon.getEndTime();
        this.loanId = userCoupon.getLoanId();
        this.used = InvestStatus.SUCCESS == userCoupon.getStatus();
        this.expired = !this.used && new DateTime(this.endTime).plusDays(1).withTimeAtStartOfDay().isBeforeNow();
        this.unused = !this.used && !this.expired;
        this.investLowerLimit = coupon.getInvestLowerLimit();
    }

    public UserCouponDto(CouponModel couponModel, UserCouponModel userCouponModel, long investAmount) {
        this(couponModel, userCouponModel);
        this.usable = this.unused && investAmount >= couponModel.getInvestLowerLimit();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isUnused() {
        return unused;
    }

    public void setUnused(boolean unused) {
        this.unused = unused;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(long investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }
}
