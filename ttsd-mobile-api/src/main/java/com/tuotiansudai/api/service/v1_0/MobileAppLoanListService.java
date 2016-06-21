package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoanListRequestDto;

public interface MobileAppLoanListService {
    BaseResponseDto generateLoanList(LoanListRequestDto investListRequestDto);

    BaseResponseDto generateIndexLoan(BaseParamDto baseParamDto);

}
