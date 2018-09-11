package com.tuotiansudai.fudian.dto.response;

public class ResponseDto<T extends BaseContentDto> {

    private String reqData;

    private T content;

    private String retCode;

    private String retMsg;

    private String sign;

    private String certInfo;

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo;
    }

    public boolean isSuccess() {
        return "0000".equalsIgnoreCase(this.retCode);
    }
}
