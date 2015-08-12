package com.ttsd.api.service;

import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestRequestDto;
import com.ttsd.api.dto.InvestResponseDataDto;

import java.util.Map;

public interface MobileAppUmPayInvestService {

    BaseResponseDto invest(InvestRequestDto investRequestDto);

    String verifyInvestRequestDto(InvestRequestDto investRequestDto);

    InvestResponseDataDto convertInvestResponseDataDtoFromMap(Map<String,String> map);

    Invest createInvest(InvestRequestDto investRequestDto);
}
