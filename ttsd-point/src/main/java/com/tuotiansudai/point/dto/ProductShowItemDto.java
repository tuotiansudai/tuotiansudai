package com.tuotiansudai.point.dto;

import com.tuotiansudai.point.repository.model.ItemType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.util.AmountConverter;
import coupon.repository.model.ExchangeCouponView;

import java.util.Date;

public class ProductShowItemDto {

    private long id;
    private int seq;
    private ItemType itemType;
    private String name;
    private String description;
    private String imageUrl;
    private long leftCount;
    private long points;
    private String pictureDescription;
    private Date updatedTime;

    public ProductShowItemDto() {
    }

    public ProductShowItemDto(ProductModel productModel, ItemType itemType, String pictureDescription) {
        this.id = productModel.getId();
        this.seq = productModel.getSeq();
        this.itemType = itemType;
        this.name = productModel.getName();
        this.description = productModel.getDescription();
        this.imageUrl = productModel.getImageUrl();
        this.leftCount = productModel.getTotalCount() - productModel.getUsedCount();
        this.points = productModel.getPoints();
        this.pictureDescription = pictureDescription;
        this.updatedTime = productModel.getUpdatedTime();
    }

    public ProductShowItemDto(ExchangeCouponView exchangeCouponView) {
        this.id = exchangeCouponView.getId();
        this.seq = exchangeCouponView.getSeq();
        this.imageUrl = exchangeCouponView.getImageUrl();
        switch (exchangeCouponView.getCouponType()) {
            case RED_ENVELOPE:
                this.itemType = ItemType.RED_ENVELOPE;
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getAmount()) + "元现金红包";
                this.pictureDescription = String.valueOf(exchangeCouponView.getAmount());
                break;
            case INVEST_COUPON:
                this.itemType = ItemType.INVEST_COUPON;
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getAmount()) + "元投资体验券";
                this.pictureDescription = String.valueOf(exchangeCouponView.getAmount());
                break;
            case INTEREST_COUPON:
                this.itemType = ItemType.INTEREST_COUPON;
                this.name = exchangeCouponView.getRate() * 100 + "%加息券";
                this.pictureDescription = String.valueOf(exchangeCouponView.getRate() * 100);
                break;
        }
        this.leftCount = exchangeCouponView.getTotalCount() - exchangeCouponView.getIssuedCount();
        this.points = exchangeCouponView.getExchangePoint();
        this.updatedTime = exchangeCouponView.getUpdatedTime();
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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
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
