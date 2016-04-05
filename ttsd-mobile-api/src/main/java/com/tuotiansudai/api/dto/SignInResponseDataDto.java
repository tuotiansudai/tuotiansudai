package com.tuotiansudai.api.dto;

public class SignInResponseDataDto extends BaseResponseDataDto {

    int point;

    int signInTimes;

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
}
