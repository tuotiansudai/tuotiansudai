package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.Map;

public interface MobileAppUmPayInvestService {

    BaseResponseDto invest(InvestRequestDto investRequestDto);

    String verifyInvestRequestDto(InvestRequestDto investRequestDto);

    InvestModel createInvest(InvestRequestDto investRequestDto);
}
