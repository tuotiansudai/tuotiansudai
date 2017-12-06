package com.tuotiansudai.activity.repository.model;


import com.tuotiansudai.activity.repository.dto.BannerDto;
import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BannerModel implements Serializable {

    private long id;

    private String name;

    private String webImageUrl;

    private String appImageUrl;

    private String url;

    private String appUrl;

    private String jumpToLink;

    private String title;

    private String content;

    private String sharedUrl;

    private List<Source> source;

    private boolean authenticated;

    private int order;

    private boolean active;

    private String createdBy;

    private Date createdTime = new Date();

    private String activatedBy;

    private Date activatedTime;

    private String deactivatedBy;

    private Date deactivatedTime;

    private boolean deleted = false;

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

    public String getWebImageUrl() {
        return webImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {
        this.webImageUrl = webImageUrl;
    }

    public String getAppImageUrl() {
        return appImageUrl;
    }

    public void setAppImageUrl(String appImageUrl) {
        this.appImageUrl = appImageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
        this.source = source;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
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

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getJumpToLink() {
        return jumpToLink;
    }

    public void setJumpToLink(String jumpToLink) {
        this.jumpToLink = jumpToLink;
    }

    public BannerModel() {

    }

    public BannerModel(BannerDto bannerDto) {
        this.id = bannerDto.getId();
        this.name = bannerDto.getName();
        this.webImageUrl = bannerDto.getWebImageUrl().trim();
        this.appImageUrl = bannerDto.getAppImageUrl().trim();
        this.url = bannerDto.getUrl();
        this.appUrl = bannerDto.getAppUrl();
        this.jumpToLink = bannerDto.getJumpToLink();
        this.title = bannerDto.getTitle();
        this.content = bannerDto.getContent();
        this.sharedUrl = bannerDto.getSharedUrl();
        this.source = bannerDto.getSource();
        this.authenticated = bannerDto.isAuthenticated();
        this.activatedTime = bannerDto.getActivatedTime();
        this.deactivatedTime = bannerDto.getDeactivatedTime();
        this.order = bannerDto.getOrder();
    }

}
