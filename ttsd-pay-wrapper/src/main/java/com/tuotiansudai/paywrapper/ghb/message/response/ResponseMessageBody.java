package com.tuotiansudai.paywrapper.ghb.message.response;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.IOException;
import java.text.MessageFormat;

public class ResponseMessageBody<T extends ResponseBaseOGW> {
    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
        this.content = xmlMapper.readValue(MessageFormat.format("<XMLPARA>{0}</XMLPARA>", xmlMapper), (Class<T>) bodyClass);
        this.content = xmlMapper.readValue("<XMLPARA><OLDREQSEQNO>P2P0012016062304202HyMS9O</OLDREQSEQNO><ACNAME>张晓</ACNAME><IDTYPE>1010</IDTYPE><IDNO>4401**********0044</IDNO><MOBILE>134****0587</MOBILE><ACNO>6236882280000414248</ACNO></XMLPARA>", (Class<T>) bodyClass);
    }

    public T getContent() {
        return content;
    }
}
