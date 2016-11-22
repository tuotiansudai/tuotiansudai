package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.activity.repository.dto.PromotionDto;
import com.tuotiansudai.activity.repository.dto.PromotionStatus;

import java.io.Serializable;
import java.util.Date;

public class PromotionModel implements Serializable {

    private long id;

    private String name;

    private String imageUrl;

    private String linkUrl;

    private Date startTime;

    private Date endTime;

    private long seq;

    private PromotionStatus status;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String deactivatedBy;

    private Date deactivatedTime;

    private boolean deleted;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDeactivatedBy() {
        return deactivatedBy;
    }

    public void setDeactivatedBy(String deactivatedBy) {
        this.deactivatedBy = deactivatedBy;
    }

    public Date getDeactivatedTime() {
        return deactivatedTime;
    }

    public void setDeactivatedTime(Date deactivatedTime) {
        this.deactivatedTime = deactivatedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public PromotionModel() {

    }

    public PromotionModel(PromotionDto promotionDto) {
        this.name = promotionDto.getName();
        this.imageUrl = promotionDto.getImageUrl();
        this.linkUrl = promotionDto.getLinkUrl();
        this.startTime = promotionDto.getStartTime();
        this.endTime = promotionDto.getEndTime();
        this.seq = promotionDto.getSeq();
    }
}
