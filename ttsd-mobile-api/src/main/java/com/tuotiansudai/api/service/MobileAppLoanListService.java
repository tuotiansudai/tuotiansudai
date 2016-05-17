package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.repository.model.LoanModel;

import java.util.List;

public interface MobileAppLoanListService {

    BaseResponseDto<LoanListResponseDataDto> generateLoanList(LoanListRequestDto investListRequestDto);

    BaseResponseDto<LoanListResponseDataDto> generateIndexLoan(BaseParamDto baseParamDto);

}
