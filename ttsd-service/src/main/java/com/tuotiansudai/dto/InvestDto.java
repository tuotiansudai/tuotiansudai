package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuotiansudai.repository.model.Source;

import javax.validation.constraints.Pattern;

public class InvestDto extends ProjectTransferDto {

    private Source source = Source.WEB;

    private String channel = null;
    @JsonIgnore
    private String userCouponId ;


    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }
    @JsonIgnore
    public long getUserCouponIdLong(){
        return Long.parseLong(this.getUserCouponId());
    }
}
