package com.tuotiansudai.api.dto;


import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class NewBieCouponResponseDataDto extends BaseResponseDataDto{
    private String name;

    private String amount;

    private String startTime;

    private String endTime;

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
    public NewBieCouponResponseDataDto(){

    }
    public NewBieCouponResponseDataDto(UserCouponDto userCouponDto){
        this.name = userCouponDto.getName();
        this.amount = AmountConverter.convertCentToString(userCouponDto.getAmount());
        this.startTime = new SimpleDateFormat("yyyy-MM-dd").format(userCouponDto.getStartTime());
        this.endTime = new SimpleDateFormat("yyyy-MM-dd").format(userCouponDto.getEndTime());
    }

}
