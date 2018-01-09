package com.tuotiansudai.point.repository.model;

import com.tuotiansudai.point.repository.dto.ChannelPointDetailDto;

import java.io.Serializable;
import java.util.Date;

public class ChannelPointDetailModel implements Serializable {
    private long id;
    private long channelPointId;
    private String loginName;
    private String userName;
    private String mobile;
    private String channel;
    private long point;
    private boolean success;
    private Date createdTime;
    private String remark;

    public ChannelPointDetailModel() {
    }

    public ChannelPointDetailModel(long channelPointId,ChannelPointDetailDto channelPointDetailDto) {
        this.channelPointId = channelPointId;
        this.loginName = channelPointDetailDto.getLoginName();
        this.userName = channelPointDetailDto.getUserName();
        this.mobile = channelPointDetailDto.getMobile();
        this.channel = channelPointDetailDto.getChannel();
        this.point = channelPointDetailDto.getPoint();
        this.success = channelPointDetailDto.isSuccess();
        this.remark = channelPointDetailDto.getRemark();
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChannelPointId() {
        return channelPointId;
    }

    public void setChannelPointId(long channelPointId) {
        this.channelPointId = channelPointId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMapKey() {
        return String.format("%s:%s", this.mobile, this.userName);
    }

}
