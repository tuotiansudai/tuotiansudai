package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.point.repository.model.ProductModel;

import java.util.List;

public class ProductDetailResponseDto extends BaseResponseDataDto{
    private String productId;

    private String imageUrl;

    private String points;

    private String name;

    private String goodsType;

    private List<String> productDes;

    private String leftCount;

    public ProductDetailResponseDto(String productId, String imageUrl, String name, String points) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.points = points;
        this.name = name;
    }

    public ProductDetailResponseDto(ProductModel productModel){
        this.productId = String.valueOf(productModel.getId());
        this.name = productModel.getName();
        this.goodsType = productModel.getType().getDescription();
        this.imageUrl = productModel.getImageUrl();
        this.points = String.valueOf(productModel.getPoints());
        this.leftCount = String.valueOf(productModel.getTotalCount() - productModel.getUsedCount());
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
