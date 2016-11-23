package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.activity.repository.model.ActivityModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.Date;

public class ActivityCenterDataDto {
    @ApiModelProperty(value = "描述", example = "红包、会员、神秘大奖应有尽有！")
    private String descTitle;
    @ApiModelProperty(value = "图片地址", example = "13800138000")
    private String imageUrl;
    @ApiModelProperty(value = "活动链接", example = "https://tuotiansudai.com/activity/hero-ranking")
    private String activityUrl;
    @ApiModelProperty(value = "标题", example = "周年庆")
    private String title;
    @ApiModelProperty(value = "分享链接", example = "https://tuotiansudai.com/activity/hero-ranking")
    private String sharedUrl;
    @ApiModelProperty(value = "活动内容", example = "不知道为什么，就想送你超级会员，红包，还有神秘大奖")
    private String content;
    @ApiModelProperty(value = "活动开始时间", example = "Fri Jul 22 17:59:32 CST 2016")
    private Date activatedTime;
    @ApiModelProperty(value = "活动结束时间", example = "Fri Jul 22 17:59:33 CST 2016")
    private Date expiredTime;


    public ActivityCenterDataDto() {
    }

    public ActivityCenterDataDto(ActivityModel activityModel) {
        this.descTitle = activityModel.getDescription();
        this.imageUrl = activityModel.getAppPictureUrl();
        this.activityUrl = activityModel.getAppActivityUrl();
        this.title = activityModel.getTitle();
        this.content = activityModel.getShareContent();
        this.sharedUrl = activityModel.getShareUrl();
        this.activatedTime = activityModel.getActivatedTime();
        this.expiredTime = activityModel.getExpiredTime();
    }

    public String getDescTitle() {
        return descTitle;
    }

    public void setDescTitle(String descTitle) {
        this.descTitle = descTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
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

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }
}
