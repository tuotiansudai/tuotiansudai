package com.tuotiansudai.paywrapper.ghb.message.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.asyncresponse.AsyncResponseMessageBody;
import org.hibernate.validator.constraints.Length;

/**
 * 银行发起，只有当成功时候才有返回
 */
public class ResponseOGW00045 extends ResponseBaseOGW {

    private final String asyncResponseTranscode = "OGWR0003";

    @JacksonXmlProperty(localName = "OLDREQSEQNO")
    @Length(max = 56)
    private String oldreqseqno; //原开户交易流水号

    @JacksonXmlProperty(localName = "ORDERSTATUS")
    @Length(max = 40)
    private String orderstatus; //订单状态 ORDER_COMPLETED：订单完成 ORDER_PRE_AUTHING：订单预授权中（非实时到账，下一工作日到账）

    @Override
    protected AsyncResponseMessageBody generateAsyncResponse() {
        return new AsyncResponseMessageBody(asyncResponseTranscode, oldreqseqno);
    }
}
