package com.ttsd.api.service;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanDto;
import com.ttsd.api.dto.LoanListRequestDto;

import java.util.List;

public interface MobileLoanListAppService {
    BaseResponseDto generateLoanList(LoanListRequestDto investListRequestDt);

    List<LoanDto> convertLoanDto(List<Loan> loanList);
}
