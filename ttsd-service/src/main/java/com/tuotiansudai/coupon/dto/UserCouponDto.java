package com.tuotiansudai.coupon.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class UserCouponDto implements Serializable {
    private long id;
    private String name;
    private long amount;
    private Date startTime;
    private Date endTime;
    private long loanId;
    private boolean used;
    private boolean expired;
    private boolean valid = true;
    private long couponId;
    private long investQuota;

    public UserCouponDto(CouponModel coupon, UserCouponModel userCoupon) {
        this.id = userCoupon.getId();
        this.couponId = coupon.getId();
        this.name = coupon.getName();
        this.amount = coupon.getAmount();
        this.startTime = coupon.getStartTime();
        this.endTime = coupon.getEndTime();
        this.loanId = userCoupon.getLoanId();
        this.used = (this.loanId != 0);
        if (this.used) {
            this.expired = false;
        } else {
            this.expired = new DateTime(this.endTime).plusDays(1).withTimeAtStartOfDay().isBeforeNow();
        }
        this.valid = !(this.used || this.expired);
        this.investQuota = coupon.getInvestQuota();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public long getInvestQuota() {
        return investQuota;
    }

    public void setInvestQuota(long investQuota) {
        this.investQuota = investQuota;
    }
}
