package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRepayListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestRepayListService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppInvestRepayListServiceImpl implements MobileAppInvestRepayListService {
    @Override
    public BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
