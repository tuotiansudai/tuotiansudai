package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00051;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * 放款结果查询
 * 第三方公司发起。交易提交我行5~10分钟后，可通过该接口查询银行处理结果。
 */
public class RequestOGW00066 extends RequestBaseOGW {

    @JsonIgnore
    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00051.class;

    @JsonIgnore
    protected RequestType requestType = RequestType.SYNC;

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno; //原放款交易流水号

    @JacksonXmlProperty(localName = "LOANNO")
    @Length(max = 128)
    @NotBlank
    private String loanno; //借款编号

    @JacksonXmlProperty(localName = "OLDTTJNL")
    @Length(max = 56)
    private String oldttjnl = ""; //原投标流水号

    public RequestOGW00066(String oldreqseqno, long loanId) {
        super(Source.WEB, "OGW00066", IdGenerator.generate());
        this.oldreqseqno = oldreqseqno;
        this.loanno = String.valueOf(loanId);
    }
}
