package com.tuotiansudai.api.dto;


import com.tuotiansudai.coupon.dto.CouponAlertDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class CouponAlertResponseDataDto extends BaseResponseDataDto{
    private String name;

    private String amount;

    private String startTime;

    private String endTime;

    private CouponType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public CouponAlertResponseDataDto(){

    }
    public CouponAlertResponseDataDto(CouponAlertDto couponAlertDto){
        this.type = couponAlertDto.getCouponType();
        this.name = couponAlertDto.getCouponType().getName();
        this.amount = AmountConverter.convertCentToString(couponAlertDto.getAmount());
        this.endTime = new SimpleDateFormat("yyyy-MM-dd").format(couponAlertDto.getExpiredDate());
    }

}
