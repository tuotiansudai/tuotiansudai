package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarListResponseDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

@Service
public class MobileAppRepayCalendarServiceImpl implements MobileAppRepayCalendarService {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM");

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendarBy(final RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear());

        List<RepayCalendarResponseDto> repayCalendarResponseDtoList = Lists.newArrayList();
        RepayCalendarResponseDto repayCalendarResponseDto = null;
        for(InvestRepayModel investRepayModel : investRepayModelList){
            if(repayCalendarResponseDto == null){
                repayCalendarResponseDto = new RepayCalendarResponseDto(investRepayModel,sdf.format(investRepayModel.getRepayDate()));
                repayCalendarResponseDtoList.add(repayCalendarResponseDto);
                continue;
            }

            if (repayCalendarResponseDto.getMonth().equals(sdf.format(investRepayModel.getRepayDate()))) {
                repayCalendarResponseDto.setExpectedRepayAmount(addMoney(repayCalendarResponseDto.getExpectedRepayAmount(),investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee()));
                repayCalendarResponseDto.setRepayAmount(addMoney(repayCalendarResponseDto.getRepayAmount(),investRepayModel.getActualInterest() - investRepayModel.getActualFee()));
                continue;
            }else{
                repayCalendarResponseDto = new RepayCalendarResponseDto(investRepayModel,sdf.format(investRepayModel.getRepayDate()));
                repayCalendarResponseDtoList.add(repayCalendarResponseDto);
            }
        }
        repayCalendarListResponseDto.setRepayCalendarResponseDtos(repayCalendarResponseDtoList);
        baseResponseDto.setData(repayCalendarListResponseDto);
        return baseResponseDto;
    }

    private String addMoney(String num1,long num2){
        return String.valueOf(Long.parseLong(num1) + num2);
    }
}
