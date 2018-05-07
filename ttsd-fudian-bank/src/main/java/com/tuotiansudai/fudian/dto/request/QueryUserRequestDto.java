package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryUserRequestDto extends BaseRequestDto {

    private String accountNo;

    private String userName;

    public QueryUserRequestDto(String accountNo, String userName, String loginName, String mobile) {
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
