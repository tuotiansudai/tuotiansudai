package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageBody;

@JacksonXmlRootElement(localName = "XMLPARA")
public class ResponseBaseOGW {

    protected AsyncResponseMessageBody generateAsyncResponse() {
        return null;
    }
}
