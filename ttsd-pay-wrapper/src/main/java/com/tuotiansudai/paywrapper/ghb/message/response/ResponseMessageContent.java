package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageContent;
import com.tuotiansudai.paywrapper.ghb.security.provider.DES;
import com.tuotiansudai.paywrapper.ghb.security.provider.XML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JacksonXmlRootElement(localName = "Document")
public class ResponseMessageContent<T extends ResponseBaseOGW> {
    private static final Log logger = LogFactory.getLog(ResponseMessageContent.class);

    @JacksonXmlProperty(localName = "header")
    private ResponseMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private ResponseMessageBody<T> body; //报文体

    @JsonIgnore
    private AsyncResponseMessageContent asyncResponse;

    @JsonIgnore
    private String plainXMLPARA;

    @JsonIgnore
    private String cipherXMLPARA;

    @JsonIgnore
    private String plainXML;

    @JsonIgnore
    private String fullMessage;

    public ResponseMessageContent() {
    }

    public ResponseMessageContent(String message, Class<? extends ResponseBaseOGW> bodyClass) throws IOException {
        this.fullMessage = message;
        this.cipherXMLPARA = this.generateCipherXMLPARA();
        this.plainXMLPARA = DES.decrypt(this.cipherXMLPARA);
        this.plainXML = this.generatePlainXML();
        ResponseMessageContent<T> response = XML.deserializer(message, new TypeReference<ResponseMessageContent<T>>() {
        });
        if (response != null) {
            this.header = response.getHeader();
            this.body = response.getBody();
            this.body.decodeXMLPARA(bodyClass);
            this.asyncResponse = new AsyncResponseMessageContent(header, body.getContent().generateAsyncResponse());
        }
    }

    private String generateCipherXMLPARA() {
        Pattern compile = Pattern.compile("^.*<XMLPARA>(.*)</XMLPARA>.*$");
        Matcher matcher = compile.matcher(this.fullMessage);

        if (!matcher.find()) {
            logger.error(MessageFormat.format("cipher XMLPARA is not exist, full message: {0}", this.fullMessage));
            return null;
        }

        return matcher.group(1);
    }

    private String generatePlainXML() {
        Pattern compile = Pattern.compile("^.*(<Document>.*<XMLPARA>).*(</XMLPARA>.*</Document>)$");
        Matcher matcher = compile.matcher(this.fullMessage);

        if (!matcher.find()) {
            logger.error(MessageFormat.format("Document is not exist, full message: {0}", this.fullMessage));
            return null;
        }

        return MessageFormat.format("{0}{1}{2}", matcher.group(1), this.plainXMLPARA, matcher.group(2));
    }

    public ResponseMessageHeader getHeader() {
        return header;
    }

    public ResponseMessageBody<T> getBody() {
        return body;
    }

    public AsyncResponseMessageContent getAsyncResponse() {
        return asyncResponse;
    }

    public String getPlainXMLPARA() {
        return plainXMLPARA;
    }

    public String getCipherXMLPARA() {
        return cipherXMLPARA;
    }

    public String getPlainXML() {
        return plainXML;
    }

    public String getFullMessage() {
        return fullMessage;
    }
}
