package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.enums.CouponType;
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

    public ProductShowItemDto(long totalCount, long usedCount, long points, Integer seq, String imageUrl, CouponType couponType, long amount , double rate, long productId){
        this.id = productId;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.goodsType = GoodsType.COUPON;
        this.leftCount = totalCount - usedCount;
        this.points = points;
        switch (couponType) {
            case RED_ENVELOPE:
                this.name = AmountConverter.convertCentToString(amount) + "元现金红包";
                this.pictureDescription = String.valueOf(amount);
                break;
            case INVEST_COUPON:
                this.name = AmountConverter.convertCentToString(amount) + "元投资体验券";
                this.pictureDescription = String.valueOf(amount);
                break;
            case INTEREST_COUPON:
                this.name = rate * 100 + "%加息券";
                this.pictureDescription = String.valueOf(rate * 100);
                break;
        }
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
