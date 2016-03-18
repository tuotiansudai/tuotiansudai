package com.tuotiansudai.coupon.repository.model;


import java.io.Serializable;

public class CouponExchangeModel implements Serializable {

    private long id;

    private long couponId;

    private long exchangePoint;

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

    public long getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }
}
