package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@JacksonXmlRootElement(localName = "BWLIST")
public class RequestOGW00051BWLISTItem {

    public static final Class PARENT_REQUEST = RequestOGW00051.class;

    @JacksonXmlProperty(localName = "BWACNAME")
    @Length(max = 256)
    @NotBlank
    private String bwacname; //借款人姓名

    @JacksonXmlProperty(localName = "BWIDTYPE")
    @Length(max = 8)
    private String bwidtype = ""; //借款人证件类型

    @JacksonXmlProperty(localName = "BWIDNO")
    @Length(max = 64)
    private String bwidno = ""; //借款人证件号码

    @JacksonXmlProperty(localName = "BWACNO")
    @Length(max = 64)
    @NotBlank
    private String bwacno; //借款人账号

    @JacksonXmlProperty(localName = "BWACBANKID")
    @Length(max = 128)
    private String bwacbankid = ""; //借款人账号所属行名

    @JacksonXmlProperty(localName = "BWAMT")
    @Length(max = 32)
    @NotBlank
    private String bwamt; //借款人金额

    @JacksonXmlProperty(localName = "MORTGAGEID")
    @Length(max = 32)
    private String mortgageid = ""; //借款人抵押品编号

    @JacksonXmlProperty(localName = "MORTGAGEINFO")
    @Length(max = 32)
    private String mortgageinfo = ""; //借款人抵押品简单描述

    @JacksonXmlProperty(localName = "CHECKDATE")
    @Length(max = 16)
    private String checkdate = ""; //借款人审批通过日期

    @JacksonXmlProperty(localName = "REMARK")
    @Length(max = 2056)
    private String remark = ""; //备注

    public RequestOGW00051BWLISTItem(String agentUserName, String agentAccount, long amount) {
        this.bwacname = agentUserName;
        this.bwacno = agentAccount;
        this.bwamt = AmountConverter.convertCentToString(amount);
    }
}
