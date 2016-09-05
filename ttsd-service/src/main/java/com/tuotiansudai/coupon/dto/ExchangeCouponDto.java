package com.tuotiansudai.coupon.dto;


import com.tuotiansudai.coupon.repository.model.CouponModel;

import java.io.Serializable;

public class ExchangeCouponDto extends CouponDto implements Serializable{

    private Long exchangePoint;

    private Integer seq;

    private String imageUrl;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ExchangeCouponDto() {

    }

    public ExchangeCouponDto(CouponModel couponModel) {
        super(couponModel);
    }
}
