package com.tuotiansudai.api.service.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.LoanDetailV3RequestDto;
import com.tuotiansudai.api.dto.v3_0.LoanDetailV3ResponseDataDto;

public interface MobileAppLoanDetailV3Service {

    BaseResponseDto<LoanDetailV3ResponseDataDto> findLoanDetail(LoanDetailV3RequestDto requestDto);
}
