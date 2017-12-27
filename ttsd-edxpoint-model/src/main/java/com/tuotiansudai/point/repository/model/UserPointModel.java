package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserPointModel implements Serializable {
    private long id;
    private String loginName;
    private long point;
    private long sudaiPoint;
    private long channelPoint;
    private String channel;
    private Date updatedTime;

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

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getSudaiPoint() {
        return sudaiPoint;
    }

    public void setSudaiPoint(long sudaiPoint) {
        this.sudaiPoint = sudaiPoint;
    }

    public long getChannelPoint() {
        return channelPoint;
    }

    public void setChannelPoint(long channelPoint) {
        this.channelPoint = channelPoint;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
