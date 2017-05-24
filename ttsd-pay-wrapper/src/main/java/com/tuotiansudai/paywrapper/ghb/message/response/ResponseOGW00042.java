package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageBody;
import org.hibernate.validator.constraints.Length;

/**
 * 银行发起，只有当成功时候才有返回
 */
public class ResponseOGW00042 extends ResponseBaseOGW {

    private final String asyncResponseTranscode = "OGWR0001";

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    private String oldreqseqno; //原开户交易流水号

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    private String acname; //姓名 可为空

    @JacksonXmlProperty(localName = "IDTYPE")
    @Length(max = 8)
    private String idtype; //证件类型 1010: 居民身份证 可为空

    @JacksonXmlProperty(localName = "IDNO")
    @Length(max = 64)
    private String idno; //身份证 可为空

    @JacksonXmlProperty(localName = "MOBILE")
    @Length(max = 36)
    private String mobile; //手机号 可为空

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    private String acno = ""; //银行账号

    @Override
    protected AsyncResponseMessageBody generateAsyncResponse() {
        return new AsyncResponseMessageBody(asyncResponseTranscode, oldreqseqno);
    }
}
