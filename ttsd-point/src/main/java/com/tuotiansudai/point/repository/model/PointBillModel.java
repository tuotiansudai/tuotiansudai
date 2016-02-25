package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PointBillModel implements Serializable {

    private long id;

    private Long orderId;

    private String loginName;

    private long point;

    private PointBusinessType businessType;

    private String note;

    private Date createdTime;

    public PointBillModel() {
    }

    public PointBillModel(String loginName, Long orderId, long point, PointBusinessType businessType, String note) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.point = point;
        this.businessType = businessType;
        this.note = note;
        this.createdTime = new Date();
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
}
