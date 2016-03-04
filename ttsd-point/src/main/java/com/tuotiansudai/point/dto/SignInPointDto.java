package com.tuotiansudai.point.dto;

import com.tuotiansudai.dto.BaseDataDto;

import java.io.Serializable;
import java.util.Date;

public class SignInPointDto extends BaseDataDto implements Serializable {

    private int signInCount;

    private Date signInDate;

    private int signInPoint;

    private int nextSignInPoint;

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

    public SignInPointDto(int signInCount, Date signInDate, int signInPoint,int nextSignInPoint) {
        this.signInCount = signInCount;
        this.signInDate = signInDate;
        this.nextSignInPoint = nextSignInPoint;
        this.signInPoint = signInPoint;
    }

    public SignInPointDto() {
    }
}
