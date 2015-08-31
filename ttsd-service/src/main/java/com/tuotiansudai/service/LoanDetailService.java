package com.tuotiansudai.service;

import com.tuotiansudai.dto.InvestRecordRequestDto;
import com.tuotiansudai.dto.InvestRecordResponseDto;
import com.tuotiansudai.dto.LoanDetailDto;

public interface LoanDetailService {

    LoanDetailDto getLoanDetail(String loanId);

    String getExpectedTotalIncome(String loanId,String investAmount);

    InvestRecordResponseDto getInvests(InvestRecordRequestDto dto);

}
