package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleModel;

import java.util.List;

public interface LoanCreateService {

    List<LoanTitleModel> findAllTitles();

    LoanTitleModel createTitle(LoanTitleDto loanTitleDto);

    BaseDto<BaseDataDto> createLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<BaseDataDto> updateLoan(LoanCreateRequestDto loanCreateRequestDto);

    LoanCreateRequestDto getEditLoanDetails(long loanId);

    BaseDto<PayDataDto> openLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> delayLoan(LoanCreateRequestDto loanCreateRequestDto);

    void startRaising(long loanId);

    BaseDto<PayDataDto> loanOut(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> cancelLoan(LoanCreateRequestDto loanCreateRequestDto);

    BaseDto<PayDataDto> applyAuditLoan(long loanId);
}
