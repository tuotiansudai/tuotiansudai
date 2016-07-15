package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MobileAppRepayCalendarServiceImpl implements MobileAppRepayCalendarService {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    private SimpleDateFormat yearMonthSdf = new SimpleDateFormat("MM");

    private SimpleDateFormat daySdf = new SimpleDateFormat("dd");

    private SimpleDateFormat querySdf = new SimpleDateFormat("yyyy-MM");

    private static List<String> monthList = Lists.newArrayList("01","02","03","04","05","06","07","08","09","10","11","12");

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = getRepayCalendarResponseList(repayCalendarRequestDto,yearMonthSdf);
        for(RepayCalendarYearResponseDto repayCalendarYearResponseDto : repayCalendarYearResponseDtoList){
            monthList.remove(repayCalendarYearResponseDto.getMonth());
        }
        for(String month : monthList){
            repayCalendarYearResponseDtoList.add(new RepayCalendarYearResponseDto(month,"0","0"));
        }

        Collections.sort(repayCalendarYearResponseDtoList, new Comparator<RepayCalendarYearResponseDto>() {
            @Override
            public int compare(RepayCalendarYearResponseDto o1, RepayCalendarYearResponseDto o2) {
                return Integer.compare(Integer.parseInt(o1.getMonth()),Integer.parseInt(o2.getMonth()));
            }
        });
        repayCalendarListResponseDto.setRepayCalendarYearResponseDtos(repayCalendarYearResponseDtoList);
        baseResponseDto.setData(repayCalendarListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<RepayCalendarMonthResponseDto> getMonthRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarMonthResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarMonthResponseDto repayCalendarMonthResponseDto = new RepayCalendarMonthResponseDto();
        List<RepayCalendarYearResponseDto> repayCalendarDtoLists = getRepayCalendarResponseList(repayCalendarRequestDto,daySdf);
        List<String> repayDayList = Lists.newArrayList();
        long expectedRepayAmount = 0;
        long repayAmount = 0;
        for(RepayCalendarYearResponseDto repayCalendarYearResponseDto: repayCalendarDtoLists){
            repayDayList.add(repayCalendarYearResponseDto.getMonth());
            expectedRepayAmount += repayCalendarYearResponseDto.getExpectedRepayAmount() != null ? Long.parseLong(repayCalendarYearResponseDto.getExpectedRepayAmount()) : 0;
            repayAmount += repayCalendarYearResponseDto.getRepayAmount() != null ? Long.parseLong(repayCalendarYearResponseDto.getRepayAmount()) : 0;
        }
        repayCalendarMonthResponseDto.setRepayDate(repayDayList);
        repayCalendarMonthResponseDto.setExpectedRepayAmount(String.valueOf(expectedRepayAmount));
        repayCalendarMonthResponseDto.setRepayAmount(String.valueOf(repayAmount));
        baseResponseDto.setData(repayCalendarMonthResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<RepayCalendarDateListResponseDto> getDateRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        BaseResponseDto<RepayCalendarDateListResponseDto> baseResponseDto = new BaseResponseDto<>();
        List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList = Lists.newArrayList();
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),null,null,repayCalendarRequestDto.getDate());
        long repayExpectedInterest;
        long repayActualInterest;
        for(InvestRepayModel investRepayModel : investRepayModelList){
            List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(investRepayModel.getInvestId(),null,repayCalendarRequestDto.getDate());
            repayExpectedInterest = investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
            repayActualInterest = investRepayModel.getActualInterest() - investRepayModel.getActualFee();
            for(CouponRepayModel couponRepayModel: couponRepayModelList){
                repayExpectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                repayActualInterest += couponRepayModel.getActualInterest() - couponRepayModel.getActualFee();
            }
            repayCalendarDateResponseDtoList.add(new RepayCalendarDateResponseDto(loanMapper.findById(investMapper.findById(investRepayModel.getInvestId()).getLoanId()).getName(),
                    String.valueOf(repayActualInterest),
                    String.valueOf(repayExpectedInterest),
                    String.valueOf(investRepayModel.getPeriod()),
                    String.valueOf(investRepayMapper.findByInvestIdAndPeriodAsc(investRepayModel.getInvestId()).size()),
                    investRepayModel.getStatus().name()));
        }

        RepayCalendarDateListResponseDto repayCalendarDateListResponseDto = new RepayCalendarDateListResponseDto();
        repayCalendarDateListResponseDto.setRepayCalendarDateResponseDtoList(repayCalendarDateResponseDtoList);
        baseResponseDto.setData(repayCalendarDateListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    private List<RepayCalendarYearResponseDto> getRepayCalendarResponseList(RepayCalendarRequestDto repayCalendarRequestDto,SimpleDateFormat sdf){
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear(),repayCalendarRequestDto.getMonth(),null);
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = Lists.newArrayList();
        RepayCalendarYearResponseDto repayCalendarYearResponseDto = null;
        for(InvestRepayModel investRepayModel : investRepayModelList){
            if(repayCalendarYearResponseDto == null){
                repayCalendarYearResponseDto = new RepayCalendarYearResponseDto(investRepayModel,sdf);
                repayCalendarYearResponseDtoList.add(setExpectedAndActualAmount(repayCalendarYearResponseDto,investRepayModel));
                continue;
            }

            if (repayCalendarYearResponseDto.getMonth().equals(sdf.format(investRepayModel.getRepayDate()))) {
                setExpectedAndActualAmount(repayCalendarYearResponseDto,investRepayModel);
                continue;
            }

            repayCalendarYearResponseDto = new RepayCalendarYearResponseDto(investRepayModel,sdf);
            repayCalendarYearResponseDtoList.add(setExpectedAndActualAmount(repayCalendarYearResponseDto,investRepayModel));
        }
        return repayCalendarYearResponseDtoList;
    }

    private RepayCalendarYearResponseDto setExpectedAndActualAmount(RepayCalendarYearResponseDto repayCalendarYearResponseDto, InvestRepayModel investRepayModel){
        List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(investRepayModel.getInvestId(),querySdf.format(investRepayModel.getRepayDate()),null);
        for(CouponRepayModel couponRepayModel: couponRepayModelList){
            repayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(repayCalendarYearResponseDto.getExpectedRepayAmount(),couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee()));
            repayCalendarYearResponseDto.setRepayAmount(addMoney(repayCalendarYearResponseDto.getRepayAmount(),couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()));
        }
        repayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(repayCalendarYearResponseDto.getExpectedRepayAmount(),investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee()));
        repayCalendarYearResponseDto.setRepayAmount(addMoney(repayCalendarYearResponseDto.getRepayAmount(),investRepayModel.getActualInterest() - investRepayModel.getActualFee()));
        return repayCalendarYearResponseDto;
    }

    private String addMoney(String num1,long num2){
        return String.valueOf(Long.parseLong(Strings.isNullOrEmpty(num1) ? "0" : num1) + num2);
    }
}
