package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.PromotionModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PromotionDto {

    private long id;

    private String name;

    private String imageUrl;

    private String linkUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private long seq;

    public PromotionDto(){

    }

    public PromotionDto(PromotionModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.imageUrl = model.getImageUrl();
        this.linkUrl = model.getLinkUrl();
        this.startTime = model.getStartTime();
        this.endTime = model.getEndTime();
        this.seq = model.getSeq();
    }

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
}
