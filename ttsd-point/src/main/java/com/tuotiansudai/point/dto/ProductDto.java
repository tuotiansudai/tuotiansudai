package com.tuotiansudai.point.dto;


import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class ProductDto implements Serializable {
    private long id;
    private String loginName;
    private GoodsType type;
    private long couponId;
    private String name;
    private Integer seq;
    private String imageUrl;
    private String description;
    private long totalCount;
    private long points;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public ProductDto(){

    }

    public ProductDto(ExchangeCouponDto exchangeCouponDto, String loginName){
        this.type = GoodsType.COUPON;
        this.loginName = loginName;
        this.couponId = exchangeCouponDto.getId();
        this.name = exchangeCouponDto.getCouponType().getName();
        this.seq = exchangeCouponDto.getSeq();
        this.imageUrl = exchangeCouponDto.getImageUrl();
        this.points = exchangeCouponDto.getExchangePoint();
        this.totalCount = exchangeCouponDto.getTotalCount();
        this.startTime = exchangeCouponDto.getStartTime();
        this.endTime = exchangeCouponDto.getEndTime();
    }

    public ProductDto(ExchangeCouponDto exchangeCouponDto, String loginName, ProductModel productModel){
        this.id = productModel.getId();
        this.type = GoodsType.COUPON;
        this.loginName = loginName;
        this.couponId = exchangeCouponDto.getId();
        this.name = exchangeCouponDto.getCouponType().getName();
        this.seq = exchangeCouponDto.getSeq();
        this.imageUrl = exchangeCouponDto.getImageUrl();
        this.points = exchangeCouponDto.getExchangePoint();
        this.totalCount = exchangeCouponDto.getTotalCount();
        this.startTime = exchangeCouponDto.getStartTime();
        this.endTime = exchangeCouponDto.getEndTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
