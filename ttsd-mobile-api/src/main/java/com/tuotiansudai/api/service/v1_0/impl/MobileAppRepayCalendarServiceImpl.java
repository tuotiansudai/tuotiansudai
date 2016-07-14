package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MobileAppRepayCalendarServiceImpl implements MobileAppRepayCalendarService {

    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private CouponRepayMapper couponRepayMapper;

    private SimpleDateFormat monthSdf = new SimpleDateFormat("MM");

    private SimpleDateFormat daySdf = new SimpleDateFormat("dd");

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        repayCalendarListResponseDto.setRepayCalendarResponseDtos(getRepayCalendarResponseList(investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear(),null),monthSdf));
        baseResponseDto.setData(repayCalendarListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getMonthRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        repayCalendarListResponseDto.setRepayCalendarResponseDtos(getRepayCalendarResponseList(investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear(),repayCalendarRequestDto.getMonth()),daySdf));
        baseResponseDto.setData(repayCalendarListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    private List<RepayCalendarResponseDto> getRepayCalendarResponseList(List<InvestRepayModel> investRepayModelList,SimpleDateFormat sdf){
        List<RepayCalendarResponseDto> repayCalendarResponseDtoList = Lists.newArrayList();
        RepayCalendarResponseDto repayCalendarResponseDto = null;
        for(InvestRepayModel investRepayModel : investRepayModelList){
            String s = sdf.format(investRepayModel.getRepayDate());
            if(repayCalendarResponseDto == null){
                repayCalendarResponseDto = new RepayCalendarResponseDto(investRepayModel,sdf);
                repayCalendarResponseDtoList.add(repayCalendarResponseDto);
                continue;
            }

            if (sdf.toPattern().equals("MM") && repayCalendarResponseDto.getMonth().equals(sdf.format(investRepayModel.getRepayDate())) ||
                    sdf.toPattern().equals("dd") && repayCalendarResponseDto.getRepayDate().equals(sdf.format(investRepayModel.getRepayDate()))) {
                setExpectedAndActualAmount(repayCalendarResponseDto,investRepayModel);
                continue;
            }

            repayCalendarResponseDto = new RepayCalendarResponseDto(investRepayModel,sdf);
            repayCalendarResponseDtoList.add(repayCalendarResponseDto);
        }
        return repayCalendarResponseDtoList;
    }

    private RepayCalendarResponseDto setExpectedAndActualAmount(RepayCalendarResponseDto repayCalendarResponseDto,InvestRepayModel investRepayModel){

        long investRepay = investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
        repayCalendarResponseDto.setExpectedRepayAmount(addMoney(repayCalendarResponseDto.getExpectedRepayAmount(),investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee()));
        repayCalendarResponseDto.setRepayAmount(addMoney(repayCalendarResponseDto.getRepayAmount(),investRepayModel.getActualInterest() - investRepayModel.getActualFee()));
        return repayCalendarResponseDto;
    }

    private String addMoney(String num1,long num2){
        return String.valueOf(Long.parseLong(num1) + num2);
    }
}
