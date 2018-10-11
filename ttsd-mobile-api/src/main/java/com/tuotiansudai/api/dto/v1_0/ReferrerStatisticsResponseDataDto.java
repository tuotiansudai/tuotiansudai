package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class ReferrerStatisticsResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "已获得推荐奖励金额总计", example = "1000")
    public String rewardAmount;

    @ApiModelProperty(value = "已推荐人数", example = "10")
    public String referrersSum;

    @ApiModelProperty(value = "推荐人出借总额", example = "1000")
    public String referrersInvestAmount;

    @ApiModelProperty(value = "banner列表", example = "list")
    public BannerPictureResponseDataDto banner;

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getReferrersSum() {
        return referrersSum;
    }

    public void setReferrersSum(String referrersSum) {
        this.referrersSum = referrersSum;
    }

    public String getReferrersInvestAmount() {
        return referrersInvestAmount;
    }

    public void setReferrersInvestAmount(String referrersInvestAmount) {
        this.referrersInvestAmount = referrersInvestAmount;
    }

    public BannerPictureResponseDataDto getBanner() {
        return banner;
    }

    public void setBanner(BannerPictureResponseDataDto banner) {
        this.banner = banner;
    }
}
