package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PointBillModel implements Serializable {

    private long id;

    private Long orderId;

    private String loginName;

    private long point;

    private long sudaiPoint;

    private long channelPoint;

    private PointBusinessType businessType;

    private String note;

    private Date createdTime;

    private String mobile;

    private String userName;

    public PointBillModel() {
    }

    public PointBillModel(String loginName, Long orderId, long sudaiPoint, long channelPoint, PointBusinessType businessType, String note,String mobile,String userName) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.point = sudaiPoint + channelPoint;
        this.sudaiPoint = sudaiPoint;
        this.channelPoint = channelPoint;
        this.businessType = businessType;
        this.note = note;
        this.createdTime = new Date();
        this.mobile = mobile;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public PointBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(PointBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
