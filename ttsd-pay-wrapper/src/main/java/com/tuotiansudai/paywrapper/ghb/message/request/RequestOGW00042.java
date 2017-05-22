package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00042;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 账户开立
 * 由第三方公司发起，跳转到银行官网完成进行该操作。交易提交我行10分钟后，可通过该接口查询银行处理结果。客户在页面流程操作共不可超过20分钟，否则请求超时。
 */
public class RequestOGW00042 extends RequestBaseOGW implements AsyncRequestBaseOGW {

    @JsonIgnore
    protected String pcTranscode = "OGW00042";

    @JsonIgnore
    protected String appTranscode = "OGW00090";

    @JsonIgnore
    protected RequestType requestType = RequestType.ASYNC;

    @JsonIgnore
    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00042.class;

    @JacksonXmlProperty(localName = "TTRANS")
    @Length(max = 4)
    @NotBlank
    private String ttrans = "6"; //账户开立

    @JacksonXmlProperty(localName = "MERCHANTNAME")
    @Length(max = 256)
    @NotBlank
    private String merchantname = "拓天速贷"; //商户名称

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    private String acname = ""; //姓名 可为空

    @JacksonXmlProperty(localName = "IDTYPE")
    @Length(max = 8)
    private String idtype = "1010"; //证件类型 1010: 居民身份证 可为空

    @JacksonXmlProperty(localName = "IDNO")
    @Length(max = 64)
    private String idno = ""; //身份证 可为空

    @JacksonXmlProperty(localName = "MOBILE")
    @Length(max = 36)
    private String mobile = ""; //手机号 可为空

    @JacksonXmlProperty(localName = "EMAIL")
    @Length(max = 100)
    private String email = ""; //邮箱 可为空

    @JacksonXmlProperty(localName = "RETURNURL")
    @Length(max = 256)
    private String returnurl = ""; //返回商户URL 可为空

    @JacksonXmlProperty(localName = "CUSTMNGRNO")
    @Length(max = 100)
    private String custmngrno = ""; //客户经理编号 可为空

    public RequestOGW00042(Source source, String userName, String identityNumber, String mobile) {
        super(source, Lists.newArrayList(Source.ANDROID, Source.IOS, Source.MOBILE).contains(source) ? "OGW00090" : "OGW00042", IdGenerator.generate());
        this.acname = userName;
        this.idno = identityNumber;
        this.mobile = mobile;
    }
}
