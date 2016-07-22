package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailRequestDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailResponseDataDto;

public interface MobileAppLoanDetailV2Service {

    BaseResponseDto<LoanDetailResponseDataDto> findLoanDetail(LoanDetailRequestDto requestDto);
}
