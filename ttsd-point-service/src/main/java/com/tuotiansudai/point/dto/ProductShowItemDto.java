package com.tuotiansudai.point.dto;

import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class ProductShowItemDto {

    private long id;
    private int seq;
    private GoodsType goodsType;
    private String name;
    private String description;
    private String imageUrl;
    private long leftCount;
    private long points;
    private String pictureDescription;
    private Date updatedTime;

    public ProductShowItemDto() {
    }

    public ProductShowItemDto(ProductModel productModel, GoodsType goodsType, String pictureDescription) {
        this.id = productModel.getId();
        this.seq = productModel.getSeq();
        this.goodsType = goodsType;
        this.name = productModel.getName();
        this.description = productModel.getDescription();
        this.imageUrl = productModel.getImageUrl();
        this.leftCount = productModel.getTotalCount() - productModel.getUsedCount();
        this.points = productModel.getPoints();
        this.pictureDescription = pictureDescription;
        this.updatedTime = productModel.getUpdatedTime();
    }

    public ProductShowItemDto(ExchangeCouponView exchangeCouponView, long productId) {
        this.id = productId;
        this.seq = exchangeCouponView.getSeq();
        this.imageUrl = exchangeCouponView.getImageUrl();
        switch (exchangeCouponView.getCouponModel().getCouponType()) {
            case RED_ENVELOPE:
                this.goodsType = GoodsType.COUPON;
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getCouponModel().getAmount()) + "元现金红包";
                this.pictureDescription = String.valueOf(exchangeCouponView.getCouponModel().getAmount());
                break;
            case INVEST_COUPON:
                this.goodsType = GoodsType.COUPON;
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getCouponModel().getAmount()) + "元投资体验券";
                this.pictureDescription = String.valueOf(exchangeCouponView.getCouponModel().getAmount());
                break;
            case INTEREST_COUPON:
                this.goodsType = GoodsType.COUPON;
                this.name = exchangeCouponView.getCouponModel().getRate() * 100 + "%加息券";
                this.pictureDescription = String.valueOf(exchangeCouponView.getCouponModel().getRate() * 100);
                break;
        }
        this.leftCount = exchangeCouponView.getCouponModel().getTotalCount() - exchangeCouponView.getCouponModel().getIssuedCount();
        this.points = exchangeCouponView.getExchangePoint();
        this.updatedTime = exchangeCouponView.getCouponModel().getUpdatedTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(long leftCount) {
        this.leftCount = leftCount;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
