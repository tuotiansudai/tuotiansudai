package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryLogAccountRequestDto extends BaseRequestDto {

    private String userName;

    private String accountNo;

    private String queryOrderDate;

    public QueryLogAccountRequestDto(String userName, String accountNo, String queryOrderDate) {
        super(ApiType.QUERY_LOG_ACCOUNT.name().toLowerCase());
        this.userName = userName;
        this.accountNo = accountNo;
        this.queryOrderDate = queryOrderDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getQueryOrderDate() {
        return queryOrderDate;
    }

    public void setQueryOrderDate(String queryOrderDate) {
        this.queryOrderDate = queryOrderDate;
    }
}
