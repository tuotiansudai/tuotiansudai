package com.ttsd.api.dto;

import java.util.Map;

/**
 * Created by tuotian on 15/8/10.
 */
public class BankCardResponseDto extends BaseResponseDataDto{
    private Map requestData;//请求参数

    public Map getRequestData() {
        return requestData;
    }

    public void setRequestData(Map requestData) {
        this.requestData = requestData;
    }
}
