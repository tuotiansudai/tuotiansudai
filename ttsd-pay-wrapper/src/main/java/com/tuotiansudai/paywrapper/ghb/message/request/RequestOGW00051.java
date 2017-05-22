package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00045;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00051;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * 单笔发标信息通知
 * 由第三方公司发起。发标的融资标的信息，可通过单笔通讯或者文件同步方式。这涉及到投标、流标等控制。具体方式根据协商确定。（首次投产时，建议第三方公司提供一个全量文件给银行一次性导入，以后就单笔通知）。如没收到银行的处理结果，可对同一标的多次发送，但发请求时，报文头的流水号需每次保证唯一性，当报文头返回错误码是“EAS020420026”时则代表原标的已成功，无需再重复发送。
 * 此接口支持新标的通知和债券转让标的通知。
 * 债券转让申请(OGW00063)我行返回成功后，第三方公司审核允许转让后需调用此接口进行转让标的发标申请。
 */
public class RequestOGW00051 extends RequestBaseOGW {

    @JsonIgnore
    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00051.class;

    @JsonIgnore
    protected RequestType requestType = RequestType.SYNC;

    @JacksonXmlProperty(localName = "MERCHANTNAME")
    @Length(max = 256)
    @NotBlank
    private String merchantname = "拓天速贷"; //商户名称

    @JacksonXmlProperty(localName = "LOANNO")
    @Length(max = 128)
    @NotBlank
    private String loanno; //借款编号 目前两者为一致

    @JacksonXmlProperty(localName = "INVESTID")
    @Length(max = 258)
    @NotBlank
    private String investid; //标的编号 目前两者为一致

    @JacksonXmlProperty(localName = "INVESTOBJNAME")
    @Length(max = 1024)
    @NotBlank
    private String investobjname; //标的名称

    @JacksonXmlProperty(localName = "INVESTOBJINFO")
    @Length(max = 2056)
    private String investobjinfo = ""; //标的简介

    @JacksonXmlProperty(localName = "MININVESTAMT")
    @Length(max = 32)
    private String mininvestamt = ""; //最低投标金额

    @JacksonXmlProperty(localName = "MAXINVESTAMT")
    @Length(max = 32)
    private String maxinvestamt = ""; //最高投标金额

    @JacksonXmlProperty(localName = "INVESTOBJAMT")
    @Length(max = 32)
    private String investobjamt; //总标的金额

    @JacksonXmlProperty(localName = "INVESTBEGINDATE")
    @Length(max = 16)
    @NotBlank
    private String investbegindate; //招标开始日期

    @JacksonXmlProperty(localName = "INVESTENDDATE")
    @Length(max = 16)
    @NotBlank
    private String investenddate; //招标到期日期

    @JacksonXmlProperty(localName = "REPAYDATE")
    @Length(max = 16)
    private String repaydate = ""; //还款日期

    @JacksonXmlProperty(localName = "YEARRATE")
    @Length(max = 20)
    @NotBlank
    private String yearrate; //年利率

    @JacksonXmlProperty(localName = "INVESTRANGE")
    @Length(max = 20)
    @NotBlank
    private String investrange; //期限

    @JacksonXmlProperty(localName = "RATESTYPE")
    @Length(max = 256)
    private String ratestype = ""; //计息方式

    @JacksonXmlProperty(localName = "REPAYSTYPE")
    @Length(max = 256)
    private String repaystype = ""; //还款方式

    @JacksonXmlProperty(localName = "INVESTOBJSTATE")
    @Length(max = 6)
    @NotBlank
    private String investobjstate = "0"; //标的状态 0 正常 1 撤销

    @JacksonXmlProperty(localName = "BWTOTALNUM")
    @Length(max = 20)
    @NotBlank
    private String bwtotalnum = "1"; //借款人总数

    @JacksonXmlProperty(localName = "ZRFLAG")
    @Length(max = 20)
    @NotBlank
    private String zrflag; //是否为债券转让标的 0 否，1 是

    @JacksonXmlProperty(localName = "REFLOANNO")
    @Length(max = 128)
    @NotBlank
    private String refloanno; //债券转让原标的 当ZRFLAG=1时必填

    @JacksonXmlProperty(localName = "OLDREQSEQ")
    @Length(max = 56)
    @NotBlank
    private String oldreqseq; //原投标第三方交易流水号 当ZRFLAG=1时必填

    @JacksonXmlProperty(localName = "BWLIST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RequestOGW00051BWLISTItem> bwlist; //借款人列表<BWLIST>(目前只支持一个)

    public RequestOGW00051(long businessId, long loanId, String loanName, long amount, Date deadline, String agentUserName, String agentAccount) {
        super(Source.WEB, "OGW00051", businessId);
        this.loanno = String.valueOf(loanId);
        this.investid = String.valueOf(loanId);
        this.investobjname = loanName;
        this.investobjamt = AmountConverter.convertCentToString(amount);
        this.repaydate = new DateTime(deadline).toString("yyyyMMdd");

        this.bwlist = Lists.newArrayList(new RequestOGW00051BWLISTItem(agentUserName, agentAccount, amount), new RequestOGW00051BWLISTItem(agentUserName, agentAccount, amount));
    }
}
