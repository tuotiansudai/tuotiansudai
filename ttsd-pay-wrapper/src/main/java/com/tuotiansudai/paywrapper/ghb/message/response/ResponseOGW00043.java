package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageBody;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ResponseOGW00043 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno; //原开户交易流水号

    @JacksonXmlProperty(localName = "RETURN_STATUS")
    @Length(max = 2)
    @NotBlank
    private String return_status; // S 成功 F 失败 R 处理中（客户仍停留在页面操作，25分钟后仍是此状态可置交易为失败） N 未知（已提交后台，需再次发查询接口。）

    @JacksonXmlProperty(localName = "ERRORMSG")
    @Length(max = 64)
    private String errormsg; //失败才返回

    @JacksonXmlProperty(localName = "RESJNLNO")
    @Length(max = 64)
    private String resjnlno; //失败才返回

    @JacksonXmlProperty(localName = "TRANSDT")
    @Length(max = 16)
    private String transdt; //交易日期

    @JacksonXmlProperty(localName = "TRANSTM")
    @Length(max = 12)
    private String transtm; //交易时间

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    private String acno; // RETURN_STATUS = S 才返回

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    private String acname; // 客户姓名

    @JacksonXmlProperty(localName = "IDTYPE")
    @Length(max = 8)
    private String idtype; // 证件类型

    @JacksonXmlProperty(localName = "IDNO")
    @Length(max = 64)
    private String idno; // 证件号码

    @JacksonXmlProperty(localName = "MOBILE")
    @Length(max = 36)
    private String mobile; // 手机号码

    public String getOldreqseqno() {
        return oldreqseqno;
    }

    public String getReturn_status() {
        return return_status;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public String getTransdt() {
        return transdt;
    }

    public String getTranstm() {
        return transtm;
    }

    public String getAcno() {
        return acno;
    }

    public String getAcname() {
        return acname;
    }

    public String getIdtype() {
        return idtype;
    }

    public String getIdno() {
        return idno;
    }

    public String getMobile() {
        return mobile;
    }
}
