package com.tuotiansudai.fudian.dto.request;

public class QueryLogLoanAccountRequestDto extends BaseRequestDto {

    private String loanAccNo;

    private String loanTxNo;

    public QueryLogLoanAccountRequestDto(String loanAccNo, String loanTxNo) {
        super(Source.WEB, null, null, null, null);
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
