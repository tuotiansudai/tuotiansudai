package com.tuotiansudai.fudian.dto.response;

import java.util.List;

public class LoanCallbackContentDto extends BaseContentDto {

    private String loanTxNo;

    private List<LoanCallbackInvestItemContentDto> investList;

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public List<LoanCallbackInvestItemContentDto> getInvestList() {
        return investList;
    }

    public void setInvestList(List<LoanCallbackInvestItemContentDto> investList) {
        this.investList = investList;
    }
}