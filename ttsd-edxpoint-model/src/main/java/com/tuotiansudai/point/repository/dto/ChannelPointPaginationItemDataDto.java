package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.ChannelPointModel;

import java.io.Serializable;
import java.util.Date;

public class ChannelPointPaginationItemDataDto implements Serializable {
    private String serialNo;
    private Date createdTime;
    private String createdBy;
    private long headCount;
    private long totalPoint;

    public ChannelPointPaginationItemDataDto() {
    }

    public ChannelPointPaginationItemDataDto(ChannelPointModel channelPointModel) {
        this.serialNo = channelPointModel.getSerialNo();
        this.createdTime = channelPointModel.getCreatedTime();
        this.createdBy = channelPointModel.getCreatedBy();
        this.headCount = channelPointModel.getHeadCount();
        this.totalPoint = channelPointModel.getTotalPoint();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getHeadCount() {
        return headCount;
    }

    public void setHeadCount(long headCount) {
        this.headCount = headCount;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }
}
