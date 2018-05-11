package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryLogLoanAccountRequestDto extends BaseRequestDto {

    private String loanAccNo;

    private String loanTxNo;

    public QueryLogLoanAccountRequestDto(String loginName, String mobile, String loanAccNo, String loanTxNo) {
        super(Source.WEB, loginName, mobile, ApiType.QUERY_LOG_LOAN_ACCOUNT);
        this.loanAccNo = loanAccNo;
        this.loanTxNo = loanTxNo;
    }

    public String getLoanAccNo() {
        return loanAccNo;
    }

    public void setLoanAccNo(String loanAccNo) {
        this.loanAccNo = loanAccNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }
}
