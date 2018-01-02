package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class ProductModel implements Serializable {

    private long id;
    private GoodsType type;
    private long couponId;
    private String name;
    private Integer seq;
    private String imageUrl;
    private String description;
    private long totalCount;
    private long monthLimit;
    private long usedCount;
    private long points;
    private long actualPoints;
    private Date startTime;
    private Date endTime;
    private boolean active;
    private String createdBy;
    private Date createdTime;
    private String activeBy;
    private Date activeTime;
    private String updatedBy;
    private Date updatedTime;
    private String webPictureUrl;
    private String appPictureUrl;


    public ProductModel() {
    }

    public ProductModel(String loginName, GoodsType type, long couponId,
                        String name, Integer seq, String imageUrl,
                        String description, long totalCount, long monthLimit,
                        long points, Date startTime, Date endTime,
                        String webPictureUrl, String appPictureUrl) {
        this.type = type;
        this.name = name;
        this.couponId = couponId;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.description = description;
        this.totalCount = totalCount;
        this.monthLimit = monthLimit;
        this.points = points;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = loginName;
        this.createdTime = new Date();
        this.webPictureUrl = webPictureUrl;
        this.appPictureUrl = appPictureUrl;
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

    public long getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(long actualPoints) {
        this.actualPoints = actualPoints;
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

    public long getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(long monthLimit) {
        this.monthLimit = monthLimit;
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

    public String getWebPictureUrl() {
        return webPictureUrl;
    }

    public void setWebPictureUrl(String webPictureUrl) {
        this.webPictureUrl = webPictureUrl;
    }

    public String getAppPictureUrl() {
        return appPictureUrl;
    }

    public void setAppPictureUrl(String appPictureUrl) {
        this.appPictureUrl = appPictureUrl;
    }
}
