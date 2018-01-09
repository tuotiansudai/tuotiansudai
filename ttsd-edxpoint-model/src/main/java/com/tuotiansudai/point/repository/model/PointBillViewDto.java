package com.tuotiansudai.point.repository.model;

import java.io.Serializable;


public class PointBillViewDto extends PointBillModel implements Serializable {
    private String userName;
    private String mobile;
    private String channel;

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
}
