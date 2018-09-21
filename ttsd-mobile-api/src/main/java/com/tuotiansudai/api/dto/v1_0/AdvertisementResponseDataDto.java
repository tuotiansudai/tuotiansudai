package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AdvertisementResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "图片地址", example = "{static}/api/images/app-advertisement/app-adv1-640-960.png")
    private String url;
    @ApiModelProperty(value = "点击链接", example = "https://tuotiansudai.com/activity/share-reward?source=app")
    private String linkedUrl;
    @ApiModelProperty(value = "标题", example = "拓天速贷")
    private String title;
    @ApiModelProperty(value = "分享链接", example = "https://tuotiansudai.com/activity/share-reward")
    private String sharedUrl;
    @ApiModelProperty(value = "内容", example = "推荐奖励:0元出借赚收益,呼朋唤友抢佣金")
    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkedUrl() {
        return linkedUrl;
    }

    public void setLinkedUrl(String linkedUrl) {
        this.linkedUrl = linkedUrl;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
