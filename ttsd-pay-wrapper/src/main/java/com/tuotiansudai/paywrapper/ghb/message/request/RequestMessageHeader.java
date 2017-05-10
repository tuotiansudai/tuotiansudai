package com.tuotiansudai.paywrapper.ghb.message.request;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.joda.time.DateTime;

import java.text.MessageFormat;

public class RequestMessageHeader {

    @JacksonXmlProperty(localName = "channelCode")
    private String channelCode = "ttsd"; //接入渠道

    @JacksonXmlProperty(localName = "channelFlow")
    private String channelFlow; //渠道流水号 渠道标识 + yyyyMMdd + 所发交易的交易码的最后三位 + 11位商户流水

    @JacksonXmlProperty(localName = "channelDate")
    private String channelDate; //渠道日期 yyyyMMdd

    @JacksonXmlProperty(localName = "channelTime")
    private String channelTime; //渠道时间 HHmmss

    @JacksonXmlProperty(localName = "encryptData")
    private String encryptData; //加密域 暂时为空


    public RequestMessageHeader(RequestBaseOGW requestBaseOGW) {
        DateTime now = new DateTime();
        this.channelDate = now.toString("yyyyMMdd");
        this.channelTime = now.toString("HHmmss");
        this.channelFlow = MessageFormat.format("{0}{1}{2}{3}",
                this.channelCode,
                this.channelDate,
                requestBaseOGW.getTranscode().substring(requestBaseOGW.getTranscode().length() - 3),
                "orderid");
    }

    public String getChannelCode() {
        return channelCode;
    }

    public String getChannelFlow() {
        return channelFlow;
    }

    public String getChannelDate() {
        return channelDate;
    }

    public String getChannelTime() {
        return channelTime;
    }

    public String getEncryptData() {
        return encryptData;
    }
}
