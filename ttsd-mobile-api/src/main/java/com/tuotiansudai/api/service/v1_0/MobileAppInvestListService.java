package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppInvestListService {


    BaseResponseDto<InvestListResponseDataDto> generateInvestList(InvestListRequestDto investListRequestDto);

    BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto requestDto);
}
