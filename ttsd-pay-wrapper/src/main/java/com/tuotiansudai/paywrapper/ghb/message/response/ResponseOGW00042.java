package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 银行发起，只有当成功时候才有返回
 */
public class ResponseOGW00042 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    private String oldreqseqno; //原开户交易流水号

    @JacksonXmlProperty(localName = "ACNAME")
    private String acname; //姓名 可为空

    @JacksonXmlProperty(localName = "IDTYPE")
    private String idtype; //证件类型 1010: 居民身份证 可为空

    @JacksonXmlProperty(localName = "IDNO")
    private String idno; //身份证 可为空

    @JacksonXmlProperty(localName = "MOBILE")
    private String mobile; //手机号 可为空

    @JacksonXmlProperty(localName = "ACNO")
    private String acno = "拓天速贷"; //商户名称
}
