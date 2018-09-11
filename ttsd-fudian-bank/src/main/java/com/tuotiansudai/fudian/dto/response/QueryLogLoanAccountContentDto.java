package com.tuotiansudai.fudian.dto.response;

import java.util.List;

public class QueryLogLoanAccountContentDto extends BaseContentDto {

    private List<QueryLogLoanAccountItemDto> loanAccountLogList;

    private String loanAccNo;

    private String loanTxNo;

    public List<QueryLogLoanAccountItemDto> getLoanAccountLogList() {
        return loanAccountLogList;
    }

    public void setLoanAccountLogList(List<QueryLogLoanAccountItemDto> loanAccountLogList) {
        this.loanAccountLogList = loanAccountLogList;
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