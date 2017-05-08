package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.Source;

/**
 * 账户开立
 * 由第三方公司发起，跳转到银行官网完成进行该操作。交易提交我行10分钟后，可通过该接口查询银行处理结果。客户在页面流程操作共不可超过20分钟，否则请求超时。
 */
public class RequestOGW00042 extends RequestBaseOGW {

    @JacksonXmlProperty(localName = "TTRANS")
    private String ttrans = "6"; //账户开立

    @JacksonXmlProperty(localName = "MERCHANTNAME")
    private String merchantname = "拓天速贷"; //商户名称

    @JacksonXmlProperty(localName = "ACNAME")
    private String acname = ""; //姓名 可为空

    @JacksonXmlProperty(localName = "IDTYPE")
    private String idtype = "1010"; //证件类型 1010: 居民身份证 可为空

    @JacksonXmlProperty(localName = "IDNO")
    private String idno = ""; //身份证 可为空

    @JacksonXmlProperty(localName = "MOBILE")
    private String mobile = ""; //手机号 可为空

    @JacksonXmlProperty(localName = "EMAIL")
    private String email = ""; //邮箱 可为空

    @JacksonXmlProperty(localName = "RETURNURL")
    private String returnurl = ""; //返回商户URL 可为空

    @JacksonXmlProperty(localName = "CUSTMNGRNO")
    private String custmngrno = ""; //客户经理编号 可为空

    public RequestOGW00042(Source source, String userName, String identityNumber, String mobile) {
        super(Lists.newArrayList(Source.ANDROID, Source.IOS, Source.MOBILE).contains(source) ? "OGW00090" : "OGW00042", source);
        this.acname = userName;
        this.idno = identityNumber;
        this.mobile = mobile;
    }
}
