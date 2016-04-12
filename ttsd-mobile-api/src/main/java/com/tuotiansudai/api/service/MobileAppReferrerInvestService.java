package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReferrerInvestListRequestDto;

public interface MobileAppReferrerInvestService {

    BaseResponseDto generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto);

}
