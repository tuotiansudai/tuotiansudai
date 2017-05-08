package com.tuotiansudai.paywrapper.ghb.message.response;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;

import javax.xml.ws.Response;
import java.io.IOException;

@JacksonXmlRootElement(localName = "Document")
public class ResponseMessageContent<T extends ResponseBaseOGW> {
    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @JacksonXmlProperty(localName = "header")
    private ResponseMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private ResponseMessageBody<T> body; //报文体

    public ResponseMessageContent() {
    }

    public ResponseMessageContent(String content, Class<? extends ResponseBaseOGW> bodyClass) throws IOException {
        ResponseMessageContent<T> response = xmlMapper.readValue(content, new TypeReference<ResponseMessageContent<T>>() {
        });
        this.header = response.getHeader();
        this.body = response.getBody();
        this.body.decodeXMLPARA(bodyClass);
    }

    public ResponseMessageHeader getHeader() {
        return header;
    }

    public ResponseMessageBody<T> getBody() {
        return body;
    }
}
