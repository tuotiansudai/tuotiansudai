package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;


public interface MobileAppLoanListV2Service {
    BaseResponseDto<LoanListResponseDataDto> generateIndexLoan(String loginName);
}
