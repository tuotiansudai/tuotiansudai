package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ResponseOGW00046 extends ResponseBaseOGW {

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno; //原充值交易流水号

    @JacksonXmlProperty(localName = "RESJNLNO")
    @Length(max = 64)
    private String resjnlno; //银行交易流水号 成功才返回

    @JacksonXmlProperty(localName = "TRANSDT")
    @Length(max = 16)
    private String transdt; //交易日期 成功才返回

    @JacksonXmlProperty(localName = "TRANSTM")
    @Length(max = 12)
    private String transtm; //交易时间 成功才返回

    /**
     *R 页面处理中（客户仍停留在页面操作，25分钟后仍收到此状态可置交易为失败）
     *N 未知（已提交后台，商户需再次发查询接口。）
     *P 预授权成功（目前未到账，下一工作日到账，当天无需再进行查询，下一工作日上午6点再进行查询，状态会变成S，如状态不变则也无需再查询，可在下一工作日在对账文件中确认交易状态。）
     *D 后台支付系统处理中（如果 ERRORMSG值为“ORDER_CREATED”，并超过25分钟未变，则可置交易是失败。其他情况商户需再次发查询接口。但2小时后状态仍未变的则可置为异常，无需再发起查询，后续在对账文件中确认交易状态或线下人工处理）
     *S 成功
     *F 失败
     */
    @JacksonXmlProperty(localName = "RETURN_STATUS")
    @Length(max = 2)
    private String return_status; //交易状态

    @JacksonXmlProperty(localName = "ERRORMSG")
    @Length(max = 64)
    private String errormsg; //失败才返回

    public String getOldreqseqno() {
        return oldreqseqno;
    }

    public String getResjnlno() {
        return resjnlno;
    }

    public String getTransdt() {
        return transdt;
    }

    public String getTranstm() {
        return transtm;
    }

    public String getReturn_status() {
        return return_status;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
