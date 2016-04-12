package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Date;
import java.util.Map;

public class BaseSyncResponseModel {

    public static String SUCCESS_CODE = "0000";

    private Long id;

    private Long requestId;

    private String signType;

    private String sign;

    private String merId;

    private String version;

    private String retCode;

    private String retMsg;

    private String responseData;

    private Date responseTime = new Date();

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(retCode);
    }

    public void initializeModel(Map<String, String> resData) {
        this.responseData = resData.toString();
        this.signType = resData.get("sign_type");
        this.sign = resData.get("sign");
        this.merId = resData.get("mer_id");
        this.version = resData.get("version");
        this.retCode = resData.get("ret_code");
        this.retMsg = resData.get("ret_msg");
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
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

        BaseSyncResponseModel that = (BaseSyncResponseModel) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
