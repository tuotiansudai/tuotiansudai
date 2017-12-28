package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class ChannelPointModel implements Serializable {
    private long id;
    private String serialNo;
    private long totalPoint;
    private long headCount;
    private String createdBy;
    private Date createdTime;

    public ChannelPointModel() {
    }

    public ChannelPointModel(String serialNo, long totalPoint, long headCount, String createdBy) {
        this.serialNo = serialNo;
        this.totalPoint = totalPoint;
        this.headCount = headCount;
        this.createdBy = createdBy;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public long getHeadCount() {
        return headCount;
    }

    public void setHeadCount(long headCount) {
        this.headCount = headCount;
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
}
