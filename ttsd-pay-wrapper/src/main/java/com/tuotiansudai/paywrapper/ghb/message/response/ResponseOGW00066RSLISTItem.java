package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@JacksonXmlRootElement(localName = "RSLIST")
public class ResponseOGW00066RSLISTItem {

    public static final Class PARENT_RESPONSE = ResponseOGW00066.class;

    @JacksonXmlProperty(localName = "REQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String reqseqno; //投标交易流水号

    @JacksonXmlProperty(localName = "LOANNO")
    @Length(max = 128)
    private String loanno; //借款编号

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    private String acno; //投资人账号

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    private String acname; //投资人账号户名

    @JacksonXmlProperty(localName = "AMOUNT")
    @Length(max = 32)
    private String amount; //投标金额

    @JacksonXmlProperty(localName = "STATUS")
    @Length(max = 4)
    private String status; //状态 L 待处理 R 正在处理 N 未明 F失败 S成功

    @JacksonXmlProperty(localName = "ERRMSG")
    @Length(max = 256)
    private String errmsg; //错误原因 STATUS=F返回。

    @JacksonXmlProperty(localName = "HOSTDT")
    @Length(max = 16)
    private String hostdt; //银行处理日期 不再返回此两值，但标签仍保留。

    @JacksonXmlProperty(localName = "HOSTJNLNO")
    @Length(max = 64)
    private String HOSTJNLNO; //银行止付流水号 不再返回此两值，但标签仍保留。
}
