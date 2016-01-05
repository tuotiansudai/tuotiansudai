package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.CouponType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class SmsCouponNotifyDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @NotNull
    private CouponType couponType;

    @NotNull
    private Date expiredDate;

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

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
}
