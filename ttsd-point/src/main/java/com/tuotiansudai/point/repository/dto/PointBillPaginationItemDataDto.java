package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.PointBillPaginationItemView;
import com.tuotiansudai.point.repository.model.PointBusinessType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by gengbeijun on 16/2/27.
 */
public class PointBillPaginationItemDataDto implements Serializable{
    private long id;

    private Long orderId;

    private String loginName;

    private long point;

    private String businessType;

    private String note;

    private Date createdTime;

    public PointBillPaginationItemDataDto(PointBillPaginationItemView view){
        this.id = view.getId();
        this.loginName = view.getLoginName();
        this.orderId = view.getOrderId();
        this.point = view.getPoint();
        this.businessType = view.getBusinessType().name();
        this.note = view.getNote();
        this.createdTime = view.getCreatedTime();
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
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
