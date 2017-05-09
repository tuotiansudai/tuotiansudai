package com.tuotiansudai.paywrapper.ghb.message.asyncresponse;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class AsyncResponseOGW {

    @JacksonXmlProperty(localName = "RETURNCODE")
    private String returncode = "000000";

    @JacksonXmlProperty(localName = "RETURNMSG")
    private String returnmsg = "交易成功";

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    private String oldreqseqno;

    public AsyncResponseOGW(String oldreqseqno) {
        this.oldreqseqno = oldreqseqno;
    }
}
