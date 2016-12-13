package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.LoanTitleModel;

import java.util.List;

public interface ConsoleLoanCreateService {
    BaseDto<BaseDataDto> createLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<BaseDataDto> updateLoan(LoanCreateRequestDto loanCreateRequestDto);

    LoanTitleModel createTitle(LoanTitleDto loanTitleDto);

    List<LoanTitleModel> findAllTitles();

    LoanCreateRequestDto getEditLoanDetails(long loanId);

    BaseDto<PayDataDto> openLoan(LoanCreateRequestDto loanCreateRequestDto, String ip);

    BaseDto<PayDataDto> delayLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> applyAuditLoan(long loanId);

    BaseDto<PayDataDto> loanOut(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> cancelLoan(LoanCreateRequestDto loanCreateRequestDto);
}
