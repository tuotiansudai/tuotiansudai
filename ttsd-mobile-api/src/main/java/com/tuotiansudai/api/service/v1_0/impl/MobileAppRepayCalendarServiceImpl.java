package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppRepayCalendarServiceImpl implements MobileAppRepayCalendarService {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Override
    public BaseResponseDto<RepayCalendarResponseDto> getYearRepayCalendarBy(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarResponseDto> baseResponseDto = new BaseResponseDto<>();
        List<InvestRepayModel> investRepayModelList =  investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear());
        return baseResponseDto;
    }
}
