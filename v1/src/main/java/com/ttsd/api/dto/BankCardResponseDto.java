package com.ttsd.api.dto;

import java.util.Map;

/**
 * Created by tuotian on 15/8/10.
 */
public class BankCardResponseDto extends BaseResponseDataDto{
    private String url;//请求地址
    private String requestData;//请求参数

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
