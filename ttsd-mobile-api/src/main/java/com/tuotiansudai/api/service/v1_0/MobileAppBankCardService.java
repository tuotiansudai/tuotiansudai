package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppBankCardService {

    BaseResponseDto<BankAsynResponseDto> bindBankCard(String loginName, String ip, String deviceId);

    BaseResponseDto<BankAsynResponseDto> unBindBankCard(String loginName, String ip, String deviceId);
}
