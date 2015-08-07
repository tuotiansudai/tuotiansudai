package com.ttsd.api.service;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanResponseDataDto;
import com.ttsd.api.dto.LoanListRequestDto;

import java.util.List;

public interface MobileAppLoanListService {
    BaseResponseDto generateLoanList(LoanListRequestDto investListRequestDt);

    List<LoanResponseDataDto> convertLoanDto(List<Loan> loanList);
}
