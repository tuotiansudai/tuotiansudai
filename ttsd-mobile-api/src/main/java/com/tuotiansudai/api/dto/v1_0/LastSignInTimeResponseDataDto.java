package com.tuotiansudai.api.dto.v1_0;

public class LastSignInTimeResponseDataDto extends BaseResponseDataDto {

    boolean signIn;

    int signInTimes;

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
