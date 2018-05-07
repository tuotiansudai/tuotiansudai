package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryUserRequestDto extends BaseRequestDto {

    private String accountNo;

    private String userName;

    public QueryUserRequestDto(String loginName, String mobile, String accountNo, String userName) {
        super(ApiType.QUERY_USER, loginName, mobile);
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
