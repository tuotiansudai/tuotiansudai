package com.tuotiansudai.point.repository.model;


import java.io.Serializable;
import java.util.Date;

public class UserPointPrizeModel implements Serializable{

    private long pointPrizeId;

    private String loginName;

    private Date createdTime;

    private boolean reality;

    public long getPointPrizeId() {
        return pointPrizeId;
    }

    public void setPointPrizeId(long pointPrizeId) {
        this.pointPrizeId = pointPrizeId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isReality() {
        return reality;
    }

    public void setReality(boolean reality) {
        this.reality = reality;
    }

    public UserPointPrizeModel() {

    }

    public UserPointPrizeModel(long pointPrizeId, String loginName, boolean reality) {
        this.pointPrizeId = pointPrizeId;
        this.loginName = loginName;
        this.createdTime = new Date();
        this.reality = reality;
    }

}
