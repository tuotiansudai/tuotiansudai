package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class SignInResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "积分", example = "20")
    int point;

    @ApiModelProperty(value = "签到次数", example = "2")
    int signInTimes;

    @ApiModelProperty(value = "下次可获得积分", example = "40")
    int nextSignInPoint;

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
}
