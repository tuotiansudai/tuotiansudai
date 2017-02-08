package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class CouponAlertResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "体验劵名称", example = "新手体验劵")
    private String name;

    @ApiModelProperty(value = "体验劵金额", example = "1000.00")
    private String amount;

    @ApiModelProperty(value = "起期", example = "2016-01-01")
    private String startTime;

    @ApiModelProperty(value = "止期", example = "2016-01-07")
    private String endTime;

    @ApiModelProperty(value = "优惠券类型", example = "NEWBIE_COUPON")
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
