package com.tuotiansudai.borrow.dto.response;


public class PayFormResponseDto {

    private String url;
    private String requestData;

    public PayFormResponseDto() {
    }

    public PayFormResponseDto(String url, String requestData) {
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
