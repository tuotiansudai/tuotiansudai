package com.ttsd.api.service;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestDto;
import com.ttsd.api.dto.InvestListRequestDto;

import java.util.List;

public interface MobileInvestListAppService {
    BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDt);

    List<InvestDto> convertLoanToInvestDto(List<Loan> loanList);
}
