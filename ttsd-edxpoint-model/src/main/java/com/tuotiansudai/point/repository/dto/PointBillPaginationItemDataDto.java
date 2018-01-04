package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBillViewDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import javafx.scene.effect.Light;

import java.io.Serializable;
import java.util.Date;

public class PointBillPaginationItemDataDto implements Serializable {
    private long id;

    private Long orderId;

    private String loginName;
    private String userName;
    private String mobile;

    private long sudaiPoint;
    private long channelPoint;
    private long point;

    private PointBusinessType businessType;

    private String note;

    private Date createdTime;
    private String channel;

    public PointBillPaginationItemDataDto(PointBillModel pointBillModel) {
        this.id = pointBillModel.getId();
        this.loginName = pointBillModel.getLoginName();
        this.orderId = pointBillModel.getOrderId();
        this.sudaiPoint = pointBillModel.getSudaiPoint();
        this.channelPoint = pointBillModel.getChannelPoint();
        this.point = pointBillModel.getPoint();
        this.businessType = pointBillModel.getBusinessType();
        this.note = pointBillModel.getNote();
        this.createdTime = pointBillModel.getCreatedTime();
    }

    public PointBillPaginationItemDataDto(PointBillViewDto dto) {
        this.id = dto.getId();
        this.businessType = dto.getBusinessType();
        this.channelPoint = dto.getChannelPoint();
        this.createdTime = dto.getCreatedTime();
        this.loginName = dto.getLoginName();
        this.userName = dto.getUserName();
        this.mobile = dto.getMobile();
        this.sudaiPoint = dto.getSudaiPoint();
        this.note = dto.getNote();
        this.point = dto.getPoint();
        this.orderId = dto.getOrderId();
        this.channel = dto.getChannel();
        this.userName = dto.getUserName();
        this.mobile = dto.getMobile();

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
