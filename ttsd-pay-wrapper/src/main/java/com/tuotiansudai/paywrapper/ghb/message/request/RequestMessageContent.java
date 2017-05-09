package com.tuotiansudai.paywrapper.ghb.message.request;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tuotiansudai.paywrapper.ghb.security.GHBSecurity;
import com.tuotiansudai.paywrapper.ghb.security.provider.DES;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JacksonXmlRootElement(localName = "Document")
public class RequestMessageContent<T extends RequestBaseOGW> {
    private static final Log logger = LogFactory.getLog(RequestMessageContent.class);

    private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    static {
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @JacksonXmlProperty(localName = "header")
    private RequestMessageHeader header; //报文头

    @JacksonXmlProperty(localName = "body")
    private RequestMessageBody<T> body; //报文体

    @JsonIgnore
    private String plainXMLPARA;

    @JsonIgnore
    private String cipherXMLPARA;

    @JsonIgnore
    private String plainXML;

    @JsonIgnore
    private String fullMessage;

    public RequestMessageContent(T ogw) throws JsonProcessingException {
        this.header = new RequestMessageHeader(ogw);
        this.body = new RequestMessageBody<>(ogw);
        this.plainXML = xmlMapper.writeValueAsString(this);
        this.plainXMLPARA = this.generatePlainXMLPARA();
        this.cipherXMLPARA = DES.decrypt(this.plainXMLPARA);
        this.fullMessage = this.generateFullMessage();
    }

    private String generatePlainXMLPARA() {
        Pattern compile = Pattern.compile("^.*<XMLPARA>(.*)</XMLPARA>.*$");
        Matcher matcher = compile.matcher(this.plainXML);

        if (!matcher.find()) {
            logger.error(MessageFormat.format("XMLPARA is not exist, plain XML: {0}", this.plainXML));
            return null;
        }

        return matcher.group(1);
    }

    private String generateFullMessage() {
        String tempXML = this.plainXML.replaceAll("<XMLPARA>(.*)</XMLPARA>", MessageFormat.format("<XMLPARA>{0}</XMLPARA>", this.cipherXMLPARA));

        return GHBSecurity.buildMessage(this.body.getXmlpara().getRequestType(), tempXML);
    }

    public RequestMessageHeader getHeader() {
        return header;
    }

    public RequestMessageBody<T> getBody() {
        return body;
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
