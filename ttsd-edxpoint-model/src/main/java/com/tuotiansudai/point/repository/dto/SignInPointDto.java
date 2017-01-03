package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.point.repository.model.PointBillModel;

import java.io.Serializable;
import java.util.Date;

public class SignInPointDto extends BaseDataDto implements Serializable {

    private int signInCount;

    private Date signInDate;

    private int signInPoint;

    private int nextSignInPoint;

    private String currentRewardDesc;

    private String nextRewardDesc;

    private boolean full;

    //发送请求时当天是否签到
    private boolean signIn;

    public SignInPointDto(int signInCount, Date signInDate, int signInPoint, int nextSignInPoint, boolean signIn) {
        this.signInCount = signInCount;
        this.signInDate = signInDate;
        this.signInPoint = signInPoint;
        this.nextSignInPoint = nextSignInPoint;
        this.signIn = signIn;
    }

    public int getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }

    public Date getSignInDate() {
        return signInDate;
    }

    public void setSignInDate(Date signInDate) {
        this.signInDate = signInDate;
    }

    public int getSignInPoint() {
        return signInPoint;
    }

    public void setSignInPoint(int signInPoint) {
        this.signInPoint = signInPoint;
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
