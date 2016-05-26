package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoanListRequestDto;
import com.tuotiansudai.api.dto.v1_0.LoanListResponseDataDto;

public interface MobileAppLoanListService {

    BaseResponseDto<LoanListResponseDataDto> generateLoanList(LoanListRequestDto investListRequestDto);

    BaseResponseDto<LoanListResponseDataDto> generateIndexLoan(BaseParamDto baseParamDto);

}
