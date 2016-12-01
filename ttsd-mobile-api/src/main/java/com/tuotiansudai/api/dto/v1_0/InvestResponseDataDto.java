package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class InvestResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "请求数据", example = "string")
    private String requestData;

    @ApiModelProperty(value = "请求url", example = "string")
    private String url;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
