package com.tuotiansudai.point.repository.model;

public enum GoodsType {

    COUPON("优惠券"),
    PHYSICAL("实物商品"),
    VIRTUAL("虚拟商品");

    private String description;

    GoodsType(String description){
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
