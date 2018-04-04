package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class WeChatUserInfoModel implements Serializable {

    private long id;
    private String openId;
    private String nickName;
    private String headImgUrl;
    private Date createdTime;
    private Date updatedTime;

    public WeChatUserInfoModel() {
    }

    public WeChatUserInfoModel(String openId, String nickName, String headImgUrl) {
        this.openId = openId;
        this.nickName = nickName;
        this.headImgUrl = headImgUrl;
        this.createdTime = new Date();
        this.updatedTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
