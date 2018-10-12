package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.point.repository.dto.SignInPointDto;
import io.swagger.annotations.ApiModelProperty;

public class SignInResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "积分", example = "20")
    private int point;

    @ApiModelProperty(value = "签到次数", example = "2")
    private int signInTimes;

    @ApiModelProperty(value = "下次可获得积分", example = "40")
    private int nextSignInPoint;

    @ApiModelProperty(value = "当前获得签到奖励", example = "获得XX元出借红包")
    private String currentRewardDesc;

    @ApiModelProperty(value = "下次获得签到奖励", example = "再签到10天可再获得10元出借红包哦")
    private String nextRewardDesc;

    @ApiModelProperty(value = "是否获得全勤奖", example = "true")
    private boolean full;

    @ApiModelProperty(value = "请求时当天是否已签到", example = "true")
    private boolean signIn;

    public SignInResponseDataDto(SignInPointDto signInPointDto) {
        this.point = signInPointDto.getSignInPoint();
        this.signInTimes = signInPointDto.getSignInCount();
        this.nextSignInPoint = signInPointDto.getNextSignInPoint();
        this.currentRewardDesc = signInPointDto.getCurrentRewardDesc();
        this.nextRewardDesc = signInPointDto.getNextRewardDesc();
        this.full = signInPointDto.isFull();
        this.signIn = signInPointDto.isSignIn();
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getSignInTimes() {
        return signInTimes;
    }

    public void setSignInTimes(int signInTimes) {
        this.signInTimes = signInTimes;
    }

    public int getNextSignInPoint() {
        return nextSignInPoint;
    }

    public void setNextSignInPoint(int nextSignInPoint) {
        this.nextSignInPoint = nextSignInPoint;
    }

    public String getCurrentRewardDesc() {
        return currentRewardDesc;
    }

    public void setCurrentRewardDesc(String currentRewardDesc) {
        this.currentRewardDesc = currentRewardDesc;
    }

    public String getNextRewardDesc() {
        return nextRewardDesc;
    }

    public void setNextRewardDesc(String nextRewardDesc) {
        this.nextRewardDesc = nextRewardDesc;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }
}
