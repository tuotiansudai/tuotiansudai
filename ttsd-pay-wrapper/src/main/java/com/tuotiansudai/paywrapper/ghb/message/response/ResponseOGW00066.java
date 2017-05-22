package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class ResponseOGW00066 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "RETURN_STATUS")
    @Length(max = 2)
    @NotBlank
    private String return_status; //交易状态 L 交易处理中 F 失败 S完成 状态为F时可为此标的重新放款,其他状态不可重新放款.

    @JacksonXmlProperty(localName = "ERRORMSG")
    @Length(max = 256)
    private String errormsg; //失败原因 STATUS=F返回。

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno; //原放款交易流水号

    @JacksonXmlProperty(localName = "LOANNO")
    @Length(max = 128)
    @NotBlank
    private String loanno; //借款编号

    @JacksonXmlProperty(localName = "BWACNAME")
    @Length(max = 256)
    @NotBlank
    private String BWACNAME; //借款人姓名

    @JacksonXmlProperty(localName = "BWACNO")
    @Length(max = 64)
    @NotBlank
    private String bwacno; //借款人账号

    @JacksonXmlProperty(localName = "ACMNGAMT")
    @Length(max = 32)
    private String acmngamt; //账户管理费

    @JacksonXmlProperty(localName = "GUARANTAMT")
    @Length(max = 32)
    private String guarantamt; //风险保证金

    @JacksonXmlProperty(localName = "RSLIST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ResponseOGW00066RSLISTItem> rslist;

    public String getReturn_status() {
        return return_status;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public String getOldreqseqno() {
        return oldreqseqno;
    }

    public String getLoanno() {
        return loanno;
    }

    public String getBWACNAME() {
        return BWACNAME;
    }

    public String getBwacno() {
        return bwacno;
    }

    public String getAcmngamt() {
        return acmngamt;
    }

    public String getGuarantamt() {
        return guarantamt;
    }

    public List<ResponseOGW00066RSLISTItem> getRslist() {
        return rslist;
    }
}
