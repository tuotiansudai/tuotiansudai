package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.security.provider.XML;

import java.io.IOException;
import java.text.MessageFormat;

public class ResponseMessageBody<T extends ResponseBaseOGW> {
    @JacksonXmlProperty(localName = "MERCHANTID")
    private String merchantid; //商户唯一标识

    @JacksonXmlProperty(localName = "TRANSCODE")
    private String transcode; //交易码

    @JacksonXmlProperty(localName = "BANKID")
    private String bankid; //银行标识 固定值：GHB

    @JacksonXmlProperty(localName = "XMLPARA")
    private String xmlpara; //加密报文体

    @JsonIgnore
    private T content; //解密报文对象

    @SuppressWarnings(value = "unchecked")
    void decodeXMLPARA(Class<? extends ResponseBaseOGW> bodyClass) throws IOException {
        this.content =  XML.deserializer(MessageFormat.format("<XMLPARA>{0}</XMLPARA>", xmlpara), (Class<T>) bodyClass);
    }

    public T getContent() {
        return content;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public String getTranscode() {
        return transcode;
    }

    public String getBankid() {
        return bankid;
    }
}
