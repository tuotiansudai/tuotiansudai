package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ResponseMessageHeader {

    @JacksonXmlProperty(localName = "channelCode")
    private String channelCode; //接入渠道

    @JacksonXmlProperty(localName = "transCode")
    private String transCode; //交易码

    @JacksonXmlProperty(localName = "channelFlow")
    private String channelFlow; //原渠道流水号

    @JacksonXmlProperty(localName = "serverFlow")
    private String serverFlow; //服务流水号

    @JacksonXmlProperty(localName = "serverDate")
    private String serverDate; //服务日期 yyyyMMdd

    @JacksonXmlProperty(localName = "serverTime")
    private String serverTime; //服务时间 HHmmss

    @JacksonXmlProperty(localName = "encryptData")
    private String encryptData; //加密域 暂时为空

    @JacksonXmlProperty(localName = "status")
    private String status; //业务状态 0：受理成功 1：受理失败 2：受理中 3：受理超时，不确定

    @JacksonXmlProperty(localName = "errorCode")
    private String errorCode; //错误代码 0：受理成功 其他：错误码

    @JacksonXmlProperty(localName = "errorMsg")
    private String errorMsg; //错误信息

    public String getChannelCode() {
        return channelCode;
    }

    public String getTransCode() {
        return transCode;
    }

    public String getChannelFlow() {
        return channelFlow;
    }

    public String getServerFlow() {
        return serverFlow;
    }

    public String getServerDate() {
        return serverDate;
    }

    public String getServerTime() {
        return serverTime;
    }

    public String getEncryptData() {
        return encryptData;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
