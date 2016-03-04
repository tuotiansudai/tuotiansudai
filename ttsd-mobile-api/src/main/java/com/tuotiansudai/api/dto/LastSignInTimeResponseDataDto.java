package com.tuotiansudai.api.dto;

public class LastSignInTimeResponseDataDto extends BaseResponseDataDto {

    boolean signIn;

    int signInTimes;

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
}
