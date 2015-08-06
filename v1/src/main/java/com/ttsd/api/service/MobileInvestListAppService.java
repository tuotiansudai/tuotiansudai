package com.ttsd.api.service;

import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestListRequestDto;
import com.ttsd.api.dto.InvestRecordDto;

import java.util.List;

public interface MobileInvestListAppService {
    BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto);

    List<InvestRecordDto> convertInvestRecordDto(List<Invest> investList);
}
