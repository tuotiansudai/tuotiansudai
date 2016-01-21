package com.tuotiansudai.coupon.dto;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponAlertDto implements Serializable {

    private List<Long> couponIds = Lists.newArrayList();

    private long amount;

    private CouponType couponType;

    private Date expiredDate;

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Long> couponIds) {
        this.couponIds = couponIds;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
}
