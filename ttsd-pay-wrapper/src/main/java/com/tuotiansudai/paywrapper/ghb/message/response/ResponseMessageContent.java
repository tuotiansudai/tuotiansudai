package com.tuotiansudai.paywrapper.ghb.message.response;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

@JacksonXmlRootElement(localName = "Document")
public class ResponseMessageContent<T extends ResponseBaseOGW> {
    private static final Log logger = LogFactory.getLog(ResponseMessageContent.class);

    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @JacksonXmlProperty(localName = "header")
    private ResponseMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private ResponseMessageBody<T> body; //报文体

    @JsonIgnore
    private AsyncResponseMessageContent asyncResponse;

    public ResponseMessageContent() {
    }

    public ResponseMessageContent(String content, Class<? extends ResponseBaseOGW> bodyClass) throws IOException {
        ResponseMessageContent<T> response = xmlMapper.readValue(content, new TypeReference<ResponseMessageContent<T>>() {
        });
        this.header = response.getHeader();
        this.body = response.getBody();
        this.body.decodeXMLPARA(bodyClass);
        this.asyncResponse = new AsyncResponseMessageContent(header, body.getContent().generateAsyncResponse());
    }

    public AsyncResponseMessageContent getAsyncResponse() {
        return asyncResponse;
    }

    public ResponseMessageHeader getHeader() {
        return header;
    }

    public ResponseMessageBody<T> getBody() {
        return body;
    }
}
