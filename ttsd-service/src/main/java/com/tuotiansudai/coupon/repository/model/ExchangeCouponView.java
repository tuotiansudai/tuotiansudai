package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.coupon.dto.CouponDto;

public class ExchangeCouponView extends CouponModel {

    private Long exchangePoint;

    private Integer seq;

    private String imageUrl;

    private long productId;

    public ExchangeCouponView() {
        super();
    }

    public ExchangeCouponView(CouponDto couponDto) {
        super(couponDto);
    }

    public ExchangeCouponView(CouponModel couponModel) {
        super(couponModel);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
