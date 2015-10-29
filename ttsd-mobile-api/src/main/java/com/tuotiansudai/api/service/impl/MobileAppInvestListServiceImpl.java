package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestListRequestDto;
import com.tuotiansudai.api.dto.InvestRecordResponseDataDto;
import com.tuotiansudai.api.dto.UserInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MobileAppInvestListServiceImpl implements MobileAppInvestListService {
    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public List<InvestRecordResponseDataDto> convertInvestRecordDto(List<InvestModel> investList) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
