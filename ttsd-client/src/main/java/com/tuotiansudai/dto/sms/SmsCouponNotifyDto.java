package com.tuotiansudai.dto.sms;

import com.tuotiansudai.enums.CouponType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class SmsCouponNotifyDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String rate;

    @NotNull
    private CouponType couponType;

    @NotNull
    private String expiredDate;

    private int deadLine;

    private int expiredCount;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;
    }

    public int getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(int expiredCount) {
        this.expiredCount = expiredCount;
    }
}
