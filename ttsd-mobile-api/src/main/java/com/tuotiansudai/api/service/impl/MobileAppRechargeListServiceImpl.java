package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RechargeListRequestDto;
import com.tuotiansudai.api.service.MobileAppRechargeListService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRechargeListServiceImpl implements MobileAppRechargeListService {
    @Override
    public BaseResponseDto generateRechargeList(RechargeListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
