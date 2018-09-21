package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class BannerPictureResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标题", example = "周年庆")
    private String title;

    @ApiModelProperty(value = "链接", example = "https://tuotiansudai.com/activity/hero-ranking")
    private String url;

    @ApiModelProperty(value = "分享链接", example = "https://tuotiansudai.com/activity/hero-ranking")
    private String sharedUrl;

    @ApiModelProperty(value = "顺序", example = "1")
    private Integer seqNum;

    @ApiModelProperty(value = "图片路径", example = "upload/20160722/4051469181629061.jpg")
    private String picture;

    @ApiModelProperty(value = "内容", example = "出借拿称号，红包奖不停")
    private String content;

    @ApiModelProperty(value = "授权", example = "false")
    private boolean isAuthorized;

    public BannerPictureResponseDataDto() {
    }

    public BannerPictureResponseDataDto(String title, String url, String sharedUrl, Integer seqNum, String picture, String content, boolean isAuthorized) {
        this.title = title;
        this.url = url;
        this.sharedUrl = sharedUrl;
        this.seqNum = seqNum;
        this.picture = picture;
        this.content = content;
        this.isAuthorized = isAuthorized;
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
