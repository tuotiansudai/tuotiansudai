package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class QueryLoanRequestDto extends BaseRequestDto {

    private String loanTxNo;

    public QueryLoanRequestDto(String loanTxNo) {
        super(ApiType.QUERY_LOAN.name().toLowerCase());
        this.loanTxNo = loanTxNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }
}
