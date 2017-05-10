package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RequestMessageBody<T extends RequestBaseOGW>{

    @JacksonXmlProperty(localName = "TRANSCODE")
    private String transcode; //交易码

    @JacksonXmlProperty(localName = "XMLPARA")
    private T xmlpara; //报文体

    public RequestMessageBody(T ogw) {
        this.transcode = ogw.getTranscode();
        this.xmlpara = ogw;
    }

    public T getXmlpara() {
        return xmlpara;
    }

    public String getTranscode() {
        return transcode;
    }
}
