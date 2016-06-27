package com.tuotiansudai.api.dto.v1_0;


public class BannerPictureResponseDataDto extends BaseResponseDataDto {

    private String pictureId;
    private String title;
    private String url;
    private String sharedUrl;
    private Integer seqNum;
    private String picture;
    private String noticeId;
    private String content;
    private boolean isAuthorized;

    public BannerPictureResponseDataDto() {
    }

    public BannerPictureResponseDataDto(String pictureId, String title, String url, String sharedUrl, Integer seqNum, String picture, String noticeId, String content, boolean isAuthorized) {
        this.pictureId = pictureId;
        this.title = title;
        this.url = url;
        this.sharedUrl = sharedUrl;
        this.seqNum = seqNum;
        this.picture = picture;
        this.noticeId = noticeId;
        this.content = content;
        this.isAuthorized = isAuthorized;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }
}
