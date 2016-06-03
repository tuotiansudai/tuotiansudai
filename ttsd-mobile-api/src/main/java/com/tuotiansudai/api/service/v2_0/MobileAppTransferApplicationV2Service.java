package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.TransferableInvestListRequestDto;

public interface MobileAppTransferApplicationV2Service {

    BaseResponseDto<UserInvestListResponseDataDto> generateTransferableInvest(TransferableInvestListRequestDto requestDto);


}
