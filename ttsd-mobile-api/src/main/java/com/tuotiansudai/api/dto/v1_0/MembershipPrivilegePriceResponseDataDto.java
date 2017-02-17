package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import io.swagger.annotations.ApiModelProperty;

public class MembershipPrivilegePriceResponseDataDto extends BaseResponseDataDto{
    @ApiModelProperty(value = "增值特权名称", example = "30天")
    private String name;

    @ApiModelProperty(value = "增值特权有效天数", example = "30")
    private int duration;

    @ApiModelProperty(value = "增值特权价格", example = "2500")
    private long price;

    public MembershipPrivilegePriceResponseDataDto(){}

    public MembershipPrivilegePriceResponseDataDto(MembershipPrivilegePriceType type){
        this.name = type.getName();
        this.duration = type.getDuration();
        this.price = type.getPrice();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
