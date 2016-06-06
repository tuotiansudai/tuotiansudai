package com.tuotiansudai.api.dto.v2_0;

import java.util.List;


public class LoanListResponseDataDto extends BaseResponseDataDto {
    private List<LoanResponseDataDto> loanList;

    public List<LoanResponseDataDto> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanResponseDataDto> loanList) {
        this.loanList = loanList;
    }
}

