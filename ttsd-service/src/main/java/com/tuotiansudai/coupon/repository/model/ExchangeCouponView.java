package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.coupon.dto.CouponDto;

public class ExchangeCouponView extends CouponModel {

    private Long exchangePoint;

    private Integer seq;

    public ExchangeCouponView() {
        super();
    }

    public ExchangeCouponView(CouponDto couponDto) {
        super(couponDto);
    }

    public Long getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(Long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
