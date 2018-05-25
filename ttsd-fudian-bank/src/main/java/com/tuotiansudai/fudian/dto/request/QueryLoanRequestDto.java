package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class QueryLoanRequestDto extends BaseRequestDto {

    private String loanTxNo;

    public QueryLoanRequestDto(String loanTxNo) {
        super(Source.WEB, null, null, ApiType.QUERY_LOAN, null);
        this.loanTxNo = loanTxNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }
}
