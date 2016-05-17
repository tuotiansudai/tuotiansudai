package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NoPasswordInvestTurnOffRequestDto;

public interface MobileAppNoPasswordInvestTurnOffService {
    BaseResponseDto noPasswordInvestTurnOff(NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto, String ip);
}
