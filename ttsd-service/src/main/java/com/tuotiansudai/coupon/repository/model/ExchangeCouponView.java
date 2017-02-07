package com.tuotiansudai.coupon.repository.model;

public class ExchangeCouponView {

    private Long exchangePoint;

    private long actualPoints;

    private Integer seq;

    private String imageUrl;

    private long productId;

    private CouponModel couponModel;

    public ExchangeCouponView() {
        super();
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

    public CouponModel getCouponModel() {
        return couponModel;
    }

    public void setCouponModel(CouponModel couponModel) {
        this.couponModel = couponModel;
    }

    public long getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(long actualPoints) {
        this.actualPoints = actualPoints;
    }

    public ExchangeCouponView(Long exchangePoint, long actualPoints, Integer seq, String imageUrl, long productId, CouponModel couponModel){
        this.exchangePoint = exchangePoint;
        this.actualPoints = actualPoints;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.couponModel = couponModel;
    }

}
