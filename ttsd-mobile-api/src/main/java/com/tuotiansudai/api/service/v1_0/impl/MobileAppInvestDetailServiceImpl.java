package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestDetailService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppInvestDetailServiceImpl implements MobileAppInvestDetailService {
    @Override
    public BaseResponseDto generateUserInvestDetail(InvestDetailRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
