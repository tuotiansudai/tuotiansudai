package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;
import java.util.List;

public class ProductDetailResponseDto extends BaseResponseDataDto {
    private String productId;

    private String imageUrl;

    private String points;

    private String name;

    private String goodsType;

    private List<String> productDes;

    private String leftCount;

    private int seq;

    private Date updatedTime;

    public ProductDetailResponseDto(long productId, String imageUrl, String name, long points, GoodsType goodsType, long leftCount) {
        this.productId = String.valueOf(productId);
        this.imageUrl = imageUrl;
        this.points = String.valueOf(points);
        this.name = name.replaceAll("\\.00", "");
        this.goodsType = goodsType.name();
        this.leftCount = String.valueOf(leftCount);
    }

    public ProductDetailResponseDto(ExchangeCouponView exchangeCouponView, String bannerServer) {
        this.productId = String.valueOf(exchangeCouponView.getProductId());
        this.imageUrl = bannerServer + exchangeCouponView.getImageUrl();
        this.points = String.valueOf(exchangeCouponView.getExchangePoint());
        this.leftCount = String.valueOf(exchangeCouponView.getTotalCount() - exchangeCouponView.getIssuedCount());
        this.seq = exchangeCouponView.getSeq();
        this.updatedTime = exchangeCouponView.getUpdatedTime();
        switch (exchangeCouponView.getCouponType()) {
            case RED_ENVELOPE:
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getAmount()) + "元现金红包";
                break;
            case INVEST_COUPON:
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getAmount()) + "元投资体验券";
                break;
            case INTEREST_COUPON:
                this.name = exchangeCouponView.getRate() * 100 + "%加息券";
                break;
        }

    }


    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public List<String> getProductDes() {
        return productDes;
    }

    public void setProductDes(List<String> productDes) {
        this.productDes = productDes;
    }

    public String getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(String leftCount) {
        this.leftCount = leftCount;
    }

}
