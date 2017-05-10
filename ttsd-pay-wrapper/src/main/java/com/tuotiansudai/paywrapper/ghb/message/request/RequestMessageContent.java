package com.tuotiansudai.paywrapper.ghb.message.request;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JacksonXmlRootElement(localName = "Document")
public class RequestMessageContent<T extends RequestBaseOGW> {
    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    @JacksonXmlProperty(localName = "header")
    private RequestMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private RequestMessageBody<T> body; //报文体

    public RequestMessageContent(T ogw) {
        this.header = new RequestMessageHeader(ogw);
        this.body = new RequestMessageBody<>(ogw);
    }

    public String generateXML() throws JsonProcessingException {
        return xmlMapper.writeValueAsString(this);
    }

    public String fetchXMLPARAContent() throws JsonProcessingException {
        String xmlpara = xmlMapper.writeValueAsString(body.getXmlpara());
        Pattern compile = Pattern.compile("^<XMLPARA>(.*)</XMLPARA>$");
        Matcher matcher = compile.matcher(xmlpara);
        return matcher.find() ? matcher.group(1) : null;
    }

    public RequestMessageHeader getHeader() {
        return header;
    }

    public RequestMessageBody<T> getBody() {
        return body;
    }
}
