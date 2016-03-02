package com.tuotiansudai.point.dto;

import com.tuotiansudai.dto.BaseDataDto;

import java.io.Serializable;
import java.util.Date;

public class SignInPointDto extends BaseDataDto {

    private int signInCount;

    private Date signInDate;

    private int point;

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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public SignInPointDto(int signInCount, Date signInDate, int point) {
        this.signInCount = signInCount;
        this.signInDate = signInDate;
        this.point = point;
    }

    public SignInPointDto() {
    }
}
