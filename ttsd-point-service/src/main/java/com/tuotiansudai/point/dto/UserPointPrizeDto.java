package com.tuotiansudai.point.dto;


import java.io.Serializable;
import java.util.Date;

public class UserPointPrizeDto implements Serializable{

    private String loginName;

    private String pointPrizeName;

    private Date createTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPointPrizeName() {
        return pointPrizeName;
    }

    public void setPointPrizeName(String pointPrizeName) {
        this.pointPrizeName = pointPrizeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserPointPrizeDto(String loginName, String pointPrizeName, Date createTime) {
        this.loginName = loginName;
        this.pointPrizeName = pointPrizeName;
        this.createTime = createTime;
    }

    public UserPointPrizeDto() {

    }

}
