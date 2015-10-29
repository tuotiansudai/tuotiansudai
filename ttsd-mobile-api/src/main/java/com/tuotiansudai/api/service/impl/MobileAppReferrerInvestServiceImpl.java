package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReferrerInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppReferrerInvestService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppReferrerInvestServiceImpl implements MobileAppReferrerInvestService {

    @Override
    public BaseResponseDto generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
