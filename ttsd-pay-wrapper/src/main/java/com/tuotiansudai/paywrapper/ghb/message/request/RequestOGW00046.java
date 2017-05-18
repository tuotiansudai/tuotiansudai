package com.tuotiansudai.paywrapper.ghb.message.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00042;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 单笔充值结果查询
 * 由第三方公司发起。
 * 交易提交我行5分钟后，可通过该接口查询银行处理结果。
 * 后续查询的频率按5分钟的时间递增。
 * 状态为S和P都可认为是交易成功，涉及显示给客户看的余额值请以我行余额查询的值为准。
 */
public class RequestOGW00046 extends RequestBaseOGW {

    protected Class<? extends ResponseBaseOGW> responseClass = ResponseOGW00042.class;

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    @NotBlank
    private String oldreqseqno = ""; //原充值报文头的"渠道流水号"

    public RequestOGW00046(String oldreqseqno) {
        super(Source.WEB, IdGenerator.generate());
        this.oldreqseqno = oldreqseqno;
    }
}
