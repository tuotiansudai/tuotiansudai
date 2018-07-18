package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class BankAsynResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "请求地址", example = "url")
    private String url;//请求地址

    @ApiModelProperty(value = "请求参数", example = "requestData")
    private String requestData;//请求参数

    public BankAsynResponseDto() {
    }

    public BankAsynResponseDto(String url, String requestData) {
        this.url = url;
        this.requestData = requestData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }
}
