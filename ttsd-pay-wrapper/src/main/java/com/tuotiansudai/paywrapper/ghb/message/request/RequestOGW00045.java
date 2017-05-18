package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00045;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 单笔专属账户充值
 * 由第三方公司发起，跳转到银行官网完成进行该操作。交易提交我行5分钟后，可通过该接口查询银行处理结果。客户在页面流程操作共不可超过20分钟，否则请求超时。
 */
public class RequestOGW00045 extends RequestBaseOGW implements AsyncRequestBaseOGW  {

    @JsonIgnore
    protected String pcTranscode = "OGW00045";

    @JsonIgnore
    protected String appTranscode = "OGW00092";

    @JsonIgnore
    protected RequestType requestType = RequestType.ASYNC;

    @JsonIgnore
    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00045.class;

    @JacksonXmlProperty(localName = "TTRANS")
    @Length(max = 4)
    @NotBlank
    private String ttrans = "7"; //充值

    @JacksonXmlProperty(localName = "MERCHANTNAME")
    @Length(max = 256)
    @NotBlank
    private String merchantname = "拓天速贷"; //商户名称

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    @NotBlank
    private String acno; //银行账号

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 40)
    @NotBlank
    private String acname = ""; //账号户名

    @JacksonXmlProperty(localName = "AMOUNT")
    @Length(max = 30)
    private String amount; //交易金额

    @JacksonXmlProperty(localName = "remark")
    @Length(max = 40)
    private String remark = ""; //备注 可为空

    @JacksonXmlProperty(localName = "RETURNURL")
    @Length(max = 256)
    private String returnurl = ""; //返回商户URL 可为空

    public RequestOGW00045(Source source, long businessId, String userName, String ghbAccount, long amount) {
        super(source, businessId);
        this.acname = userName;
        this.acno = ghbAccount;
        this.amount = AmountConverter.convertCentToString(amount);
    }
}
