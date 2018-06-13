package com.tuotiansudai.fudian.dto.request;

public class QueryLogAccountRequestDto extends BaseRequestDto {

    private String queryOrderDate;

    public QueryLogAccountRequestDto(String userName, String accountNo, String queryOrderDate) {
        super(Source.WEB, null, null, userName, accountNo);
        this.queryOrderDate = queryOrderDate;
    }

    public String getQueryOrderDate() {
        return queryOrderDate;
    }

    public void setQueryOrderDate(String queryOrderDate) {
        this.queryOrderDate = queryOrderDate;
    }
}
