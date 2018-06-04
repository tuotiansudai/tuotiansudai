package com.tuotiansudai.fudian.dto.request;

public class QueryLoanRequestDto extends BaseRequestDto {

    private String loanTxNo;

    public QueryLoanRequestDto(String loanTxNo) {
        super(Source.WEB, null, null, null, null);
        this.loanTxNo = loanTxNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }
}
