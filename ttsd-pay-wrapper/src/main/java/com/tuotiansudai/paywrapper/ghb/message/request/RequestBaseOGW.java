package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.repository.model.Source;

@JacksonXmlRootElement(localName = "XMLPARA")
public class RequestBaseOGW {

    @JsonIgnore
    protected String transcode;

    @JsonIgnore
    protected RequestType requestType = RequestType.SYNC;

    @JacksonXmlProperty(localName = "MERCHANTID")
    private String merchantid = "merchantid"; //商户唯一编号

    @JacksonXmlProperty(localName = "APPID")
    private String appid = "PC"; //应用标识 PC APP WX

    public RequestBaseOGW(String transcode, Source source) {
        this.transcode = transcode;
        this.appid = Lists.newArrayList(Source.ANDROID, Source.IOS, Source.MOBILE).contains(source) ? "APP" : "PC";
    }

    public String getTranscode() {
        return transcode;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public String getAppid() {
        return appid;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
