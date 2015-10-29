package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestListRequestDto;
import com.tuotiansudai.api.dto.InvestRecordResponseDataDto;
import com.tuotiansudai.api.dto.UserInvestListRequestDto;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;

public interface MobileAppInvestListService {


    BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto);

    List<InvestRecordResponseDataDto> convertInvestRecordDto(List<InvestModel> investList);

    BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto);
}
