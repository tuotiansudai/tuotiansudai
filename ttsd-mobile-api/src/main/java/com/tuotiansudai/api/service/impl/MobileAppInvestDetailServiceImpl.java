package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestDetailRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestDetailService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppInvestDetailServiceImpl implements MobileAppInvestDetailService {
    @Override
    public BaseResponseDto generateUserInvestDetail(InvestDetailRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
