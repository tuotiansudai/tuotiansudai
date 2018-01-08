package com.tuotiansudai.point.repository.dto;

import java.io.Serializable;

public class ChannelPointDetailDto implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private String channel;
    private long point;
    private boolean success;
    private String remark;

    public ChannelPointDetailDto() {
    }

    public ChannelPointDetailDto(String loginName, String userName, String mobile, String channel, long point) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.channel = channel;
        this.point = point;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
