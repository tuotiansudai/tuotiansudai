package com.tuotiansudai.fudian.dto.response;

import java.util.List;

public class QueryLogAccountContentDto extends BaseContentDto {

    private List<QueryLogAccountItemDto> accountLogList;

    private String userName;

    private String accountNo;

    private String queryOrderDate;

    public List<QueryLogAccountItemDto> getAccountLogList() {
        return accountLogList;
    }

    public void setAccountLogList(List<QueryLogAccountItemDto> accountLogList) {
        this.accountLogList = accountLogList;
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