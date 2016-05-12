package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestListRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestListRequestDto;

public interface MobileAppInvestListService {


    BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto);

    BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto);
}
