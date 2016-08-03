package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static final String YEAR_REPAY_CALENDAR = "YEAR_REPAY_CALENDAR";

    private static final String MONTH_REPAY_CALENDAR = "MONTH_REPAY_CALENDAR";

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto){
        List<String> monthList = Lists.newArrayList("01","02","03","04","05","06","07","08","09","10","11","12");
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = getRepayCalendarResponseList(repayCalendarRequestDto,YEAR_REPAY_CALENDAR);
        long totalRepayAmount = 0;
        long totalExpectedRepayAmount = 0;
        for(RepayCalendarYearResponseDto repayCalendarYearResponseDto : repayCalendarYearResponseDtoList){
            totalRepayAmount += Long.parseLong(Strings.isNullOrEmpty(repayCalendarYearResponseDto.getRepayAmount())? "0" : repayCalendarYearResponseDto.getRepayAmount());
            totalExpectedRepayAmount += Long.parseLong((Strings.isNullOrEmpty(repayCalendarYearResponseDto.getExpectedRepayAmount()) ? "0" : repayCalendarYearResponseDto.getExpectedRepayAmount()));
            repayCalendarYearResponseDto.setExpectedRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getExpectedRepayAmount())));
            repayCalendarYearResponseDto.setRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getRepayAmount())));
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
        repayCalendarListResponseDto.setTotalRepayAmount(String.valueOf(AmountConverter.convertCentToString(totalRepayAmount)));
        repayCalendarListResponseDto.setTotalExpectedRepayAmount(String.valueOf(AmountConverter.convertCentToString(totalExpectedRepayAmount)));
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
        List<RepayCalendarYearResponseDto> repayCalendarDtoLists = getRepayCalendarResponseList(repayCalendarRequestDto,MONTH_REPAY_CALENDAR);
        List<String> repayDayList = Lists.newArrayList();
        long expectedRepayAmount = 0;
        long repayAmount = 0;
        for(RepayCalendarYearResponseDto repayCalendarYearResponseDto: repayCalendarDtoLists){
            repayDayList.add(repayCalendarYearResponseDto.getMonth());
            expectedRepayAmount += repayCalendarYearResponseDto.getExpectedRepayAmount() != null ? Long.parseLong(repayCalendarYearResponseDto.getExpectedRepayAmount()) : 0;
            repayAmount += repayCalendarYearResponseDto.getRepayAmount() != null ? Long.parseLong(repayCalendarYearResponseDto.getRepayAmount()) : 0;
            repayCalendarYearResponseDto.setExpectedRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getExpectedRepayAmount())));
            repayCalendarYearResponseDto.setRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getRepayAmount())));
        }
        repayCalendarMonthResponseDto.setRepayDate(repayDayList);
        repayCalendarMonthResponseDto.setExpectedRepayAmount(String.valueOf(AmountConverter.convertCentToString(expectedRepayAmount)));
        repayCalendarMonthResponseDto.setRepayAmount(String.valueOf(AmountConverter.convertCentToString(repayAmount)));
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
        long repayExpectedInterest = 0;
        long repayActualInterest = 0;
        long totalAmount = 0;
        for(InvestRepayModel investRepayModel : investRepayModelList){
            List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(repayCalendarRequestDto.getBaseParam().getUserId(),null,null,repayCalendarRequestDto.getDate());
            if( investRepayModel.getActualInterest() > 0){
                repayActualInterest = investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest();
            }else{
                repayExpectedInterest = investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
            }

            for(CouponRepayModel couponRepayModel: couponRepayModelList){
                if(repayActualInterest > 0){
                    repayActualInterest += couponRepayModel.getActualInterest() - couponRepayModel.getActualFee();
                }else{
                    repayExpectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                }
            }
            repayCalendarDateResponseDtoList.add(new RepayCalendarDateResponseDto(loanMapper.findById(investMapper.findById(investRepayModel.getInvestId()).getLoanId()).getName(),
                    AmountConverter.convertCentToString(repayActualInterest),
                    AmountConverter.convertCentToString(repayExpectedInterest),
                    String.valueOf(investRepayModel.getPeriod()),
                    String.valueOf(investRepayMapper.findByInvestIdAndPeriodAsc(investRepayModel.getInvestId()).size()),
                    investRepayModel.getStatus().name()));

            if(repayActualInterest > 0){
                totalAmount += repayActualInterest;
            }else{
                totalAmount += repayExpectedInterest;
            }
        }

        RepayCalendarDateListResponseDto repayCalendarDateListResponseDto = new RepayCalendarDateListResponseDto();
        repayCalendarDateListResponseDto.setTotalAmount(AmountConverter.convertCentToString(totalAmount));
        repayCalendarDateListResponseDto.setRepayCalendarDateResponseDtoList(repayCalendarDateResponseDtoList);
        baseResponseDto.setData(repayCalendarDateListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    private List<RepayCalendarYearResponseDto> getRepayCalendarResponseList(RepayCalendarRequestDto repayCalendarRequestDto,String type){
        DateFormat sdf;
        if(type.equals(YEAR_REPAY_CALENDAR)){
            sdf = new SimpleDateFormat("MM");
        }else{
            sdf = new SimpleDateFormat("dd");
        }

        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear(),replenishMonth(repayCalendarRequestDto.getMonth()),null);
        RepayCalendarYearResponseDto repayCalendarYearResponseDto;
        Map<String,RepayCalendarYearResponseDto> repayCalendarResponseDtoMaps = Maps.newConcurrentMap();
        for(InvestRepayModel investRepayModel : investRepayModelList){
            if(investRepayModel.getActualRepayDate() != null && repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getActualRepayDate())) == null){
                repayCalendarResponseDtoMaps.put(sdf.format(investRepayModel.getActualRepayDate()),new RepayCalendarYearResponseDto(sdf.format(investRepayModel.getActualRepayDate()),investRepayModel));
                continue;
            }else{
                if(repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getRepayDate())) == null){
                    repayCalendarResponseDtoMaps.put(sdf.format(investRepayModel.getRepayDate()),new RepayCalendarYearResponseDto(sdf.format(investRepayModel.getRepayDate()),investRepayModel));
                    continue;
                }
            }

            repayCalendarYearResponseDto = repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getRepayDate()));
            if(investRepayModel.getActualInterest() > 0){
                repayCalendarYearResponseDto.setRepayAmount(addMoney(repayCalendarYearResponseDto.getRepayAmount(),String.valueOf(investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest())));
            }else{
                repayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(repayCalendarYearResponseDto.getExpectedRepayAmount(),String.valueOf(investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest())));
            }
        }

        Map<String,RepayCalendarYearResponseDto> couponRepayCalendarResponseDtoMaps = Maps.newConcurrentMap();
        RepayCalendarYearResponseDto couponRepayCalendarYearResponseDto;
        List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(repayCalendarRequestDto.getBaseParam().getUserId(),repayCalendarRequestDto.getYear(),replenishMonth(repayCalendarRequestDto.getMonth()),null);
        for(CouponRepayModel couponRepayModel : couponRepayModelList){
            if(couponRepayModel.getActualRepayDate() != null && couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getActualRepayDate())) == null){
                couponRepayCalendarResponseDtoMaps.put(sdf.format(couponRepayModel.getActualRepayDate()),new RepayCalendarYearResponseDto(sdf.format(couponRepayModel.getActualRepayDate()),couponRepayModel));
                continue;
            }else{
                if(couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getRepayDate())) == null){
                    couponRepayCalendarResponseDtoMaps.put(sdf.format(couponRepayModel.getRepayDate()),new RepayCalendarYearResponseDto(sdf.format(couponRepayModel.getRepayDate()),couponRepayModel));
                    continue;
                }
            }

            couponRepayCalendarYearResponseDto = couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getRepayDate()));
            if(couponRepayModel.getActualInterest() > 0){
                couponRepayCalendarYearResponseDto.setRepayAmount(addMoney(couponRepayCalendarYearResponseDto.getRepayAmount(),String.valueOf(couponRepayModel.getActualInterest() - couponRepayModel.getActualFee())));
            }else{
                couponRepayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(couponRepayCalendarYearResponseDto.getExpectedRepayAmount(),String.valueOf(couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee())));
            }
        }

        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = Lists.newArrayList();
        for(String key : repayCalendarResponseDtoMaps.keySet()){
            RepayCalendarYearResponseDto investRepay = repayCalendarResponseDtoMaps.get(key);
            RepayCalendarYearResponseDto couponRepay = couponRepayCalendarResponseDtoMaps.get(key);
            if(couponRepay != null){
                investRepay.setRepayAmount(addMoney(investRepay.getRepayAmount(),couponRepay.getRepayAmount()));
                investRepay.setExpectedRepayAmount(addMoney(investRepay.getExpectedRepayAmount(),couponRepay.getExpectedRepayAmount()));
            }
            repayCalendarYearResponseDtoList.add(investRepay);
        }

        return repayCalendarYearResponseDtoList;
    }

    private String addMoney(String num1,String num2){
        return String.valueOf(Long.parseLong(Strings.isNullOrEmpty(num1) ? "0" : num1) + Long.parseLong(Strings.isNullOrEmpty(num2) ? "0" : num2));
    }

    private String replenishMonth(String month){
        if(Strings.isNullOrEmpty(month)) return null;
        return month.length() == 1 ? "0" + month : month;
    }
}
