package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;

public interface CurrentRedeemService {

    BaseDto<CurrentRedeemDto> redeem(CurrentRedeemDto currentRedeemDto, String loginName);

}
