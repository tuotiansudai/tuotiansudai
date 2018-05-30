package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryTradeRequestDto extends BaseRequestDto {

    private String queryOrderNo;

    private String queryOrderDate;

    private String queryType; /*01充值 02提现 03投标 04借款人还款 05投资人回款 06债权认购 07满标放款*/

    public QueryTradeRequestDto(String queryOrderNo, String queryOrderDate, String queryType) {
        super(Source.WEB, null, null, ApiType.QUERY_TRADE, null);
        this.queryOrderNo = queryOrderNo;
        this.queryOrderDate = queryOrderDate;
        this.queryType = queryType;
    }

    public String getQueryOrderNo() {
        return queryOrderNo;
    }

    public void setQueryOrderNo(String queryOrderNo) {
        this.queryOrderNo = queryOrderNo;
    }

    public String getQueryOrderDate() {
        return queryOrderDate;
    }

    public void setQueryOrderDate(String queryOrderDate) {
        this.queryOrderDate = queryOrderDate;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
