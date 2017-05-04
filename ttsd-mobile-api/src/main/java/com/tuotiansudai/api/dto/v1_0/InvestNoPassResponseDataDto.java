package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class InvestNoPassResponseDataDto extends BaseResponseDataDto {

    public InvestNoPassResponseDataDto() {
    }

    public InvestNoPassResponseDataDto(String url) {
        this.url = url;
    }

    @ApiModelProperty(value = "请求url", example = "string")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
