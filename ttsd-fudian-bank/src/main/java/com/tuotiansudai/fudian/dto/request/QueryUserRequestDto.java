package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryUserRequestDto extends BaseRequestDto {

    private String accountNo;

    private String userName;

    public QueryUserRequestDto(String loginName, String mobile, String accountNo, String userName) {
        super(Source.WEB, loginName, mobile, ApiType.QUERY_USER, null);
        this.accountNo = accountNo;
        this.userName = userName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
