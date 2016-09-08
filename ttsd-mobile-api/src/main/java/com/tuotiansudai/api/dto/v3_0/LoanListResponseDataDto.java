package com.tuotiansudai.api.dto.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;

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
