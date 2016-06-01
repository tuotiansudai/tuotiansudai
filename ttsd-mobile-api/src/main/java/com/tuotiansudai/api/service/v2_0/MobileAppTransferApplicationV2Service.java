package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListResponseDataDto;

public interface MobileAppTransferApplicationV2Service {

    BaseResponseDto<UserInvestListResponseDataDto> generateTransferableInvest(UserInvestListRequestDto requestDto);


}
