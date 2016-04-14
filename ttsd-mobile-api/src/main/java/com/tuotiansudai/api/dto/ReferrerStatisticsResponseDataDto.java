package com.tuotiansudai.api.dto;


public class ReferrerStatisticsResponseDataDto extends BaseResponseDataDto{

    public String rewardAmount;
    public String referrersSum;
    public String referrersInvestAmount;
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
