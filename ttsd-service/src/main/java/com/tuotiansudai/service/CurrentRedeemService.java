package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.dto.RedeemLimitsDataDto;

public interface CurrentRedeemService {

    BaseDto<RedeemDataDto> redeem(CurrentRedeemDto currentRedeemDto, String loginName);

    BaseDto<RedeemLimitsDataDto> limits(String loginName);

}
