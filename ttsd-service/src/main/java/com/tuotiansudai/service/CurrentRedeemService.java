package com.tuotiansudai.service;

import com.tuotiansudai.dto.CurrentRedeemDto;

public interface CurrentRedeemService {

    void redeem(CurrentRedeemDto currentRedeemDto, String loginName);

}
