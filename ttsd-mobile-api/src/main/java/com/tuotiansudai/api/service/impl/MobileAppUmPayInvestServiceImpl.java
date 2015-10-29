package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.api.service.MobileAppUmPayInvestService;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppUmPayInvestServiceImpl implements MobileAppUmPayInvestService {

    @Override
    public BaseResponseDto invest(InvestRequestDto investRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public String verifyInvestRequestDto(InvestRequestDto investRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public InvestModel createInvest(InvestRequestDto investRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
