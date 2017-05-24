package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ResponseOGW00049 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    private String acno; //银行账号

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    private String acname; //银行户名

    @JacksonXmlProperty(localName = "ACCTBAL")
    @Length(max = 30)
    private String acctbal; //账号余额

    @JacksonXmlProperty(localName = "AVAILABLEBAL")
    @Length(max = 30)
    private String availablebal; //可用余额

    @JacksonXmlProperty(localName = "FROZBL")
    @Length(max = 30)
    private String frozbl; //冻结金额

    @JacksonXmlProperty(localName = "EXT_FILED1")
    @Length(max = 400)
    private String ext_filed1; //E账户状态 0：未激活 1：正常 A：异常 3：销户
}
