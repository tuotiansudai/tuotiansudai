package com.tuotiansudai.coupon.dto;


import com.tuotiansudai.coupon.repository.model.CouponModel;

import java.io.Serializable;

public class ExchangeCouponDto extends CouponDto implements Serializable{

    private Long exchangePoint;

    private Long seq;

    public Long getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(Long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public ExchangeCouponDto() {

    }

    public ExchangeCouponDto(CouponModel couponModel) {
        super(couponModel);
    }
}
