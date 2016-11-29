package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.dto.LoanTitleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LoanTitleModel;

import java.util.List;

public interface LoanCreateService {

    List<LoanTitleModel> findAllTitles();

    LoanTitleModel createTitle(LoanTitleDto loanTitleDto);

    LoanCreateRequestDto getEditLoanDetails(long loanId);

    BaseDto<PayDataDto> openLoan(LoanCreateRequestDto loanCreateRequestDto, String parse);

    BaseDto<PayDataDto> delayLoan(LoanCreateRequestDto loanCreateRequestDto);

    void startRaising(long loanId);

    BaseDto<PayDataDto> loanOut(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> cancelLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> applyAuditLoan(long loanId);
}
