package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00042;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 账户开立结果查询
 * 第三方公司发起账户开立结果查询， 原交易提交我行10分钟后，可通过该接口查询银行处理结果。
 * 如超过25分钟状态仍是R状态的，则认为交易是失败的，无需再过来查询。
 * 30分钟仍是未成功的状态可置交易为失败，无需再查询
 */
public class RequestOGW00043 extends RequestBaseOGW {

    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00042.class;

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno = ""; //原账户开立开立报文头的"渠道流水号"

    public RequestOGW00043(String oldreqseqno) {
        super(Source.WEB, "OGW00043", IdGenerator.generate());
        this.oldreqseqno = oldreqseqno;
    }
}
