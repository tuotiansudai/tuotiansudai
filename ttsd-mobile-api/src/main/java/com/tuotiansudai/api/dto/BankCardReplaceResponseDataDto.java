package com.tuotiansudai.api.dto;

public class BankCardReplaceResponseDataDto extends BaseResponseDataDto {
    private String url;
    private String requestData;

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
