package com.tuotiansudai.activity.repository.dto;


public class RedEnvelopSplitActivityDto {

    private String title;
    private String description;
    private String shareUrl;

    public RedEnvelopSplitActivityDto(String title, String description, String shareUrl) {
        this.title = title;
        this.description = description;
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
