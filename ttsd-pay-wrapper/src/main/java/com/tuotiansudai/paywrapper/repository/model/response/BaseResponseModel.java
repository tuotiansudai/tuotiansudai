package com.tuotiansudai.paywrapper.repository.model.response;

import java.util.Date;
import java.util.Map;

public class BaseResponseModel {

    private static String SUCCESS_CODE = "0000";

    private Long id;

    private Long requestId;

    private String returnCode;

    private String returnMessage;

    private String responseData;

    private Date responseTime = new Date();

    public void initializeModel(Map<String, String> resData) {
        this.responseData = resData.toString();
        this.returnCode = resData.get("ret_code");
        this.returnMessage = resData.get("ret_msg");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseResponseModel that = (BaseResponseModel) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(returnCode);
    }
}
