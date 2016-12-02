package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerInvestListRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerInvestListResponseDataDto;

public interface MobileAppReferrerInvestService {

    BaseResponseDto<ReferrerInvestListResponseDataDto> generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto);

}
