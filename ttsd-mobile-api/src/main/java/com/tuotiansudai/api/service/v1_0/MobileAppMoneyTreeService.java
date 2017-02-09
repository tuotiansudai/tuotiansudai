package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreeLeftCountResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreePrizeResponseDataDto;

public interface MobileAppMoneyTreeService {

    BaseResponseDto<MoneyTreeLeftCountResponseDataDto> generateLeftCount(String loginName);

    BaseResponseDto<MoneyTreePrizeResponseDataDto> generatePrize(String loginName);

}
