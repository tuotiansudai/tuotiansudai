package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReferrerInvestListRequestDto;
import com.ttsd.api.dto.ReferrerListRequestDto;

public interface MobileAppReferrerInvestService {

    BaseResponseDto generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto);

}
