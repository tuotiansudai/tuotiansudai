package com.tuotiansudai.paywrapper.ghb.message.asyncresponse;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class AsyncResponseMessageBody {

    @JacksonXmlProperty(localName = "TRANSCODE")
    private String transcode; //交易码

    @JacksonXmlProperty(localName = "XMLPARA")
    private AsyncResponseOGW xmlpara; //报文体

    public AsyncResponseMessageBody(String transcode, String oldreqseqno) {
        this.transcode = transcode;
        this.xmlpara = new AsyncResponseOGW(oldreqseqno);
    }


    public AsyncResponseOGW getXmlpara() {
        return xmlpara;
    }
}
