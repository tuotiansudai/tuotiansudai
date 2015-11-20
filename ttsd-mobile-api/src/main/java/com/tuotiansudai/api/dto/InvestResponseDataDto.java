package com.tuotiansudai.api.dto;

public class InvestResponseDataDto extends BaseResponseDataDto {
    private String requestData;
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
