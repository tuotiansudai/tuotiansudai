package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class ReferrerListRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "推荐人", example = "wangtuotian")
    private String referrerId;

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }
}
