package com.tuotiansudai.point.dto;

import com.google.common.base.Joiner;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.point.repository.model.ItemType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        this.description = generateCouponDescription(exchangeCouponView);
        this.imageUrl = "";
        this.leftCount = exchangeCouponView.getTotalCount() - exchangeCouponView.getIssuedCount();
        this.points = exchangeCouponView.getExchangePoint();
        this.updatedTime = exchangeCouponView.getUpdatedTime();
    }

    private String generateCouponDescription(ExchangeCouponView exchangeCouponView) {
        List<String> productTypeNames = new ArrayList<>();
        for (ProductType productType : exchangeCouponView.getProductTypes()) {
            productTypeNames.add(productType.getName());
        }
        String productTypeString = Joiner.on(",").join(productTypeNames);
        return MessageFormat.format("投资满{0}元即可使用;\n{1}产品可用;\n有效期限:{2}天;", exchangeCouponView.getInvestLowerLimit()/100,
                productTypeString, exchangeCouponView.getDeadline());
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
