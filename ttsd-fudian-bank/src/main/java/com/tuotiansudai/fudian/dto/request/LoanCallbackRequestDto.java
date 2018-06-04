package com.tuotiansudai.fudian.dto.request;

import java.util.List;

public class LoanCallbackRequestDto extends BaseRequestDto {

    private String loanTxNo;

    private List<LoanCallbackInvestItemRequestDto> investList;

    public LoanCallbackRequestDto(String loginName, String mobile, String loanTxNo, List<LoanCallbackInvestItemRequestDto> investList) {
        super(Source.WEB, loginName, mobile, null, null);
        this.loanTxNo = loanTxNo;
        this.investList = investList;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public List<LoanCallbackInvestItemRequestDto> getInvestList() {
        return investList;
    }

    public void setInvestList(List<LoanCallbackInvestItemRequestDto> investList) {
        this.investList = investList;
    }
}