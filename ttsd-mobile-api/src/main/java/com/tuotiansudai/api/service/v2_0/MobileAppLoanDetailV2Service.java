package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2RequestDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2ResponseDataDto;

public interface MobileAppLoanDetailV2Service {

    BaseResponseDto<LoanDetailV2ResponseDataDto> findLoanDetail(LoanDetailV2RequestDto requestDto);
}
