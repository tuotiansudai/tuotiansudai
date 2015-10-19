package com.ttsd.api.service;

import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestListRequestDto;
import com.ttsd.api.dto.InvestRecordResponseDataDto;
import com.ttsd.api.dto.UserInvestListRequestDto;

import java.util.List;

public interface MobileAppInvestListService {
    BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto);

    List<InvestRecordResponseDataDto> convertInvestRecordDto(List<Invest> investList);


    BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto);
}
