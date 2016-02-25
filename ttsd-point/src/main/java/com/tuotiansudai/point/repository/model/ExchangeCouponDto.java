package com.tuotiansudai.point.repository.model;


import com.tuotiansudai.coupon.dto.CouponDto;

import java.io.Serializable;

public class ExchangeCouponDto extends CouponDto implements Serializable{

    private long exchangePoint;

    public long getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

}
