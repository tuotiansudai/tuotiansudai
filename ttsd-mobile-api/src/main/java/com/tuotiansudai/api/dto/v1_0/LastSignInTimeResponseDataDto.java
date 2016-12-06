package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class LastSignInTimeResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "签到状态", example = "true")
    boolean signIn;

    @ApiModelProperty(value = "签到次数", example = "2")
    int signInTimes;

    @ApiModelProperty(value = "下次可获得积分", example = "40")
    int nextSignInPoint;

    public boolean isSignIn() {
        return signIn;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
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
}
