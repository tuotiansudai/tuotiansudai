package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.GoodsType;
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
    private long monthLimit;
    private long points;
    private String webPictureUrl;
    private String appPictureUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public ProductDto() {

    }

    public ProductDto(GoodsType goodsType, long couponId, String name, Integer seq, String imageUrl, long points, long totalCount, long monthLimit, Date startTime, Date endTime, String loginName) {
        this.type = goodsType;
        this.loginName = loginName;
        this.couponId = couponId;
        this.name = name;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.points = points;
        this.totalCount = totalCount;
        this.monthLimit = monthLimit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.loginName = loginName;
    }

    public ProductDto(long id, GoodsType goodsType, String loginName, long couponId, String name, Integer seq, String imageUrl, long points, long totalCount, long monthLimit, Date startTime, Date endTime) {
        this.id = id;
        this.type = goodsType;
        this.loginName = loginName;
        this.couponId = couponId;
        this.name = name;
        this.seq = seq;
        this.imageUrl = imageUrl;
        this.points = points;
        this.totalCount = totalCount;
        this.monthLimit = monthLimit;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public long getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(long monthLimit) {
        this.monthLimit = monthLimit;
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
