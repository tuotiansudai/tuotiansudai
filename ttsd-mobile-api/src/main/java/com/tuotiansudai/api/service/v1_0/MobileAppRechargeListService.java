package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RechargeListRequestDto;
import com.tuotiansudai.api.dto.v1_0.RechargeListResponseDataDto;

public interface MobileAppRechargeListService {

    BaseResponseDto<RechargeListResponseDataDto> generateRechargeList(RechargeListRequestDto requestDto);
}
