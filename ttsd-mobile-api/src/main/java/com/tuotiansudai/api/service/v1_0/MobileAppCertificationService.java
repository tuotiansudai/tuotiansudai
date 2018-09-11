package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppCertificationService {

    BaseResponseDto<BankAsynResponseDto> certificate(String loginName, String mobile, String userName, String identityNumber, String token, String ip, String deviceId);

    BaseResponseDto<BankAsynResponseDto> resetBankPassword(String loginName);
}
