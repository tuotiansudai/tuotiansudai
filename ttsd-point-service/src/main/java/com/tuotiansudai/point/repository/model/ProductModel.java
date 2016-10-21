package com.tuotiansudai.point.repository.model;

import com.tuotiansudai.point.dto.ProductDto;

import java.io.Serializable;
import java.util.Date;

public class ProductModel implements Serializable{

    private long id;
    private GoodsType type;
    private long couponId;
    private String name;
    private Integer seq;
    private String imageUrl;
    private String description;
    private long totalCount;
    private long usedCount;
    private long points;
    private Date startTime;
    private Date endTime;
    private boolean active;
    private String createdBy;
    private Date createdTime;
    private String activeBy;
    private Date activeTime;
    private String updatedBy;
    private Date updatedTime;

    public ProductModel(){

    }

    public ProductModel(ProductDto productDto) {
        this.id = productDto.getId();
        this.type = productDto.getType();
        this.couponId = productDto.getCouponId();
        this.name = productDto.getName();
        this.seq = productDto.getSeq();
        this.imageUrl = productDto.getImageUrl();
        this.description = productDto.getDescription();
        this.totalCount = productDto.getTotalCount();
        this.points = productDto.getPoints();
        this.startTime = productDto.getStartTime();
        this.endTime = productDto.getEndTime();
    }

    public ProductModel(GoodsType type, String name, Integer seq, String imageUrl, String description, long totalCount, long usedCount, long points, Date startTime, Date endTime, boolean active, String createdBy, Date createdTime) {
        this.type = type;
        this.name = name;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.description = description;
        this.totalCount = totalCount;
        this.usedCount = usedCount;
        this.points = points;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
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

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getActiveBy() {
        return activeBy;
    }

    public void setActiveBy(String activeBy) {
        this.activeBy = activeBy;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }
}
