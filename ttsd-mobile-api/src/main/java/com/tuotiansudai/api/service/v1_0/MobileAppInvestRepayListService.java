package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListResponseDataDto;

public interface MobileAppInvestRepayListService {
    BaseResponseDto<InvestRepayListResponseDataDto> generateUserInvestRepayList(InvestRepayListRequestDto requestDto);
}
