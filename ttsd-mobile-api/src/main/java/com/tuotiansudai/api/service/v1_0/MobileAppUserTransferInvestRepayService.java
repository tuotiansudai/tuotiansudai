package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayResponseDataDto;

public interface MobileAppUserTransferInvestRepayService {

    BaseResponseDto<UserTransferInvestRepayResponseDataDto> userTransferInvestRepay(UserTransferInvestRepayRequestDto userTransferInvestRepayRequestDto);

}
