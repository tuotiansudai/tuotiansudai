package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.ChannelPointDetailModel;

import java.io.Serializable;

public class ChannelPointDetailPaginationItemDataDto implements Serializable {
    private String userName;
    private String mobile;
    private String channel;
    private boolean success;
    private long point;
    private String remark;

    public ChannelPointDetailPaginationItemDataDto() {
    }

    public ChannelPointDetailPaginationItemDataDto(ChannelPointDetailModel channelPointDetailModel) {
        this.userName = channelPointDetailModel.getUserName();
        this.mobile = channelPointDetailModel.getMobile();
        this.channel = channelPointDetailModel.getChannel();
        this.success = channelPointDetailModel.isSuccess();
        this.point = channelPointDetailModel.getPoint();
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
