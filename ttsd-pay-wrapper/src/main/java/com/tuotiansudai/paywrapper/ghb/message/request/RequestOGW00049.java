package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00049;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 账户余额查询
 */
public class RequestOGW00049 extends RequestBaseOGW {

    @JsonIgnore
    protected String pcTranscode = "OGW00049";

    @JsonIgnore
    protected String appTranscode = "OGW00049";

    @JsonIgnore
    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00049.class;

    @JacksonXmlProperty(localName = "BUSTYPE")
    @Length(max = 10)
    private String bustype = ""; //业务类型 可不传字段或传空串

    @JacksonXmlProperty(localName = "ACNO")
    @Length(max = 64)
    @NotBlank
    private String acno; //银行账号

    @JacksonXmlProperty(localName = "ACNAME")
    @Length(max = 256)
    @NotBlank
    private String acname; //银行户名

    public RequestOGW00049(String userName, String ghbAccount) {
        super(Source.WEB, IdGenerator.generate());
        this.acname = userName;
        this.acno = ghbAccount;
    }
}
