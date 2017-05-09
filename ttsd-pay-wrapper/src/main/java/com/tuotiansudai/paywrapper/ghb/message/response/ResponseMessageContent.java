package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageContent;
import com.tuotiansudai.paywrapper.ghb.security.provider.XML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

@JacksonXmlRootElement(localName = "Document")
public class ResponseMessageContent<T extends ResponseBaseOGW> {
    private static final Log logger = LogFactory.getLog(ResponseMessageContent.class);

    @JacksonXmlProperty(localName = "header")
    private ResponseMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private ResponseMessageBody<T> body; //报文体

    @JsonIgnore
    private AsyncResponseMessageContent asyncResponse;

    public ResponseMessageContent() {
    }

    public ResponseMessageContent(String content, Class<? extends ResponseBaseOGW> bodyClass) throws IOException {
        ResponseMessageContent<T> response = XML.deserializer(content, new TypeReference<ResponseMessageContent<T>>() {
        });
        if (response != null) {
            this.header = response.getHeader();
            this.body = response.getBody();
            this.body.decodeXMLPARA(bodyClass);
            this.asyncResponse = new AsyncResponseMessageContent(header, body.getContent().generateAsyncResponse());
        }
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
