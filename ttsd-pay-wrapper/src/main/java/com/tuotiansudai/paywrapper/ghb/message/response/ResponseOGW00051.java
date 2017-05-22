package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.hibernate.validator.constraints.Length;

public class ResponseOGW00051 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "RESJNLNO")
    @Length(max = 64)
    private String resjnlno; //银行交易流水号

    @JacksonXmlProperty(localName = "TRANSDT")
    @Length(max = 16)
    private String transdt; //交易日期

    @JacksonXmlProperty(localName = "TRANSTM")
    @Length(max = 12)
    private String transtm; //交易时间

}
