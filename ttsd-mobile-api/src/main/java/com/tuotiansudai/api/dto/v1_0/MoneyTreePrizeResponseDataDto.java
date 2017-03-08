package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import io.swagger.annotations.ApiModelProperty;

public class MoneyTreePrizeResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "体验金额", example = "10（单位为元）")
    private String experienceAmount;

    @ApiModelProperty(value = "剩余摇奖次数", example = "4")
    private int leftCount;

    @ApiModelProperty(value = "剩余邀请好友次数", example = "2")
    private int leftInviteFriendsCount;

    public MoneyTreePrizeResponseDataDto(DrawLotteryResultDto drawLotteryResultDto) {
        this.experienceAmount = drawLotteryResultDto.getPrizeValue() != null ? drawLotteryResultDto.getPrizeValue().replace("元", "") : drawLotteryResultDto.getPrizeValue();
    }

    public String getExperienceAmount() {
        return experienceAmount;
    }

    public void setExperienceAmount(String experienceAmount) {
        this.experienceAmount = experienceAmount;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public int getLeftInviteFriendsCount() {
        return leftInviteFriendsCount;
    }

    public void setLeftInviteFriendsCount(int leftInviteFriendsCount) {
        this.leftInviteFriendsCount = leftInviteFriendsCount;
    }
}
