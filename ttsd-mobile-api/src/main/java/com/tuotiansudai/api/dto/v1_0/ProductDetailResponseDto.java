package com.tuotiansudai.api.dto.v1_0;


public class ProductDetailResponseDto {
    private String productId;

    private String imageUrl;

    private String description;

    private String points;

    public ProductDetailResponseDto(String productId, String imageUrl, String description, String points) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.points = points;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
