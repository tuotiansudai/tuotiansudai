package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.ExchangeCouponView;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class ProductDetailResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "商品ID", example = "1")
    private String productId;

    @ApiModelProperty(value = "商品图片地址", example = "url")
    private String imageUrl;

    @ApiModelProperty(value = "商品价格", example = "100")
    private String points;

    @ApiModelProperty(value = "会员折扣后商品价格", example = "90")
    private String discountPoints;

    @ApiModelProperty(value = "会员折扣描述", example = "V5会员8.5折")
    private String discountDesc;

    @ApiModelProperty(value = "商品描述", example = "会员")
    private String name;

    @ApiModelProperty(value = "商品类型", example = "PHYSICAL")
    private String goodsType;

    @ApiModelProperty(value = "商品描述", example = "[名字,介绍]")
    private List<String> productDes;

    @ApiModelProperty(value = "剩余数量", example = "199")
    private String leftCount;

    @ApiModelProperty(value = "顺序", example = "0")
    private int seq;

    @ApiModelProperty(value = "更新时间", example = "2016-11-25 14:07:01")
    private Date updatedTime;

    @ApiModelProperty(value = "商品详情图片地址", example = "/a/b/c.png")
    private String detailImage;


    public ProductDetailResponseDto(long productId, String imageUrl, String name, long points, GoodsType goodsType, long leftCount, int seq, Date updatedTime) {
        this.productId = String.valueOf(productId);
        this.imageUrl = imageUrl;
        this.points = String.valueOf(points);
        this.name = name.replaceAll("\\.00", "");
        this.goodsType = goodsType.name();
        this.leftCount = String.valueOf(leftCount);
        this.seq = seq;
        this.updatedTime = updatedTime;
    }

    public ProductDetailResponseDto(ExchangeCouponView exchangeCouponView, String bannerServer) {
        this.productId = String.valueOf(exchangeCouponView.getProductId());
        this.imageUrl = bannerServer + exchangeCouponView.getImageUrl();
        this.points = String.valueOf(exchangeCouponView.getExchangePoint());
        this.leftCount = exchangeCouponView.getCouponModel() == null ? "0" : String.valueOf(exchangeCouponView.getCouponModel().getTotalCount() - exchangeCouponView.getCouponModel().getIssuedCount());
        this.seq = exchangeCouponView.getSeq();
        this.updatedTime = exchangeCouponView.getCouponModel().getUpdatedTime();
        switch (exchangeCouponView.getCouponModel().getCouponType()) {
            case RED_ENVELOPE:
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getCouponModel().getAmount()) + "元出借红包";
                break;
            case INVEST_COUPON:
                this.name = AmountConverter.convertCentToString(exchangeCouponView.getCouponModel().getAmount()) + "元出借体验券";
                break;
            case INTEREST_COUPON:
                this.name = exchangeCouponView.getCouponModel().getRate() * 100 + "%加息券";
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

    public String getDiscountPoints() {
        return discountPoints;
    }

    public void setDiscountPoints(String discountPoints) {
        this.discountPoints = discountPoints;
    }

    public String getDiscountDesc() {
        return discountDesc;
    }

    public void setDiscountDesc(String discountDesc) {
        this.discountDesc = discountDesc;
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

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }
}
