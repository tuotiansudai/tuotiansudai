package com.tuotiansudai.paywrapper.ghb.message.asyncresponse;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseMessageHeader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JacksonXmlRootElement(localName = "Document")
public class AsyncResponseMessageContent {
    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @JacksonXmlProperty(localName = "header")
    private ResponseMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private AsyncResponseMessageBody body; //报文体

    public AsyncResponseMessageContent(ResponseMessageHeader header, AsyncResponseMessageBody body) {
        this.header = header;
        this.body = body;
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

    public ResponseMessageHeader getHeader() {
        return header;
    }

    public AsyncResponseMessageBody getBody() {
        return body;
    }
}
