package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CurrentRedeemResponseDataDto extends BaseResponseDataDto {


    @ApiModelProperty(value = "url", example = "http://xxx.xx/success.html")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
