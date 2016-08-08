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
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private static final String YEAR_REPAY_CALENDAR = "YEAR_REPAY_CALENDAR";

    private static final String MONTH_REPAY_CALENDAR = "MONTH_REPAY_CALENDAR";

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto) {
        List<String> monthList = Lists.newArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        BaseResponseDto<RepayCalendarListResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarListResponseDto repayCalendarListResponseDto = new RepayCalendarListResponseDto();
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = getRepayCalendarResponseList(repayCalendarRequestDto, YEAR_REPAY_CALENDAR);
        long totalRepayAmount = 0;
        long totalExpectedRepayAmount = 0;
        for (RepayCalendarYearResponseDto repayCalendarYearResponseDto : repayCalendarYearResponseDtoList) {
            totalRepayAmount += Long.parseLong(Strings.isNullOrEmpty(repayCalendarYearResponseDto.getRepayAmount()) ? "0" : repayCalendarYearResponseDto.getRepayAmount());
            totalExpectedRepayAmount += Long.parseLong((Strings.isNullOrEmpty(repayCalendarYearResponseDto.getExpectedRepayAmount()) ? "0" : repayCalendarYearResponseDto.getExpectedRepayAmount()));
            repayCalendarYearResponseDto.setExpectedRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getExpectedRepayAmount())));
            repayCalendarYearResponseDto.setRepayAmount(AmountConverter.convertCentToString(Long.parseLong(repayCalendarYearResponseDto.getRepayAmount())));
            monthList.remove(repayCalendarYearResponseDto.getMonth());
        }

        for (String month : monthList) {
            repayCalendarYearResponseDtoList.add(new RepayCalendarYearResponseDto(month, "0", "0"));
        }

        Collections.sort(repayCalendarYearResponseDtoList, new Comparator<RepayCalendarYearResponseDto>() {
            @Override
            public int compare(RepayCalendarYearResponseDto o1, RepayCalendarYearResponseDto o2) {
                return Integer.compare(Integer.parseInt(o1.getMonth()), Integer.parseInt(o2.getMonth()));
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
    public BaseResponseDto<RepayCalendarMonthResponseDto> getMonthRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto) {
        BaseResponseDto<RepayCalendarMonthResponseDto> baseResponseDto = new BaseResponseDto<>();
        RepayCalendarMonthResponseDto repayCalendarMonthResponseDto = new RepayCalendarMonthResponseDto();
        List<RepayCalendarYearResponseDto> repayCalendarDtoLists = getRepayCalendarResponseList(repayCalendarRequestDto, MONTH_REPAY_CALENDAR);
        List<String> repayDayList = Lists.newArrayList();
        long expectedRepayAmount = 0;
        long repayAmount = 0;
        for (RepayCalendarYearResponseDto repayCalendarYearResponseDto : repayCalendarDtoLists) {
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
    public BaseResponseDto<RepayCalendarDateListResponseDto> getDateRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto) {
        BaseResponseDto<RepayCalendarDateListResponseDto> baseResponseDto = new BaseResponseDto<>();
        List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList = Lists.newArrayList();
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(), null, null, repayCalendarRequestDto.getDate());
        long repayExpectedInterest = 0;
        long repayActualInterest = 0;
        long totalAmount = 0;
        for (InvestRepayModel investRepayModel : investRepayModelList) {

            if (investRepayModel.getActualRepayDate() != null && !formatDate.format(investRepayModel.getActualRepayDate()).equals(repayCalendarRequestDto.getDate())) {
                continue;
            }
            if (investRepayModel.getActualRepayDate() != null && investRepayModel.getRepayAmount() == 0) {
                continue;
            }

            if (investRepayModel.getActualRepayDate() != null) {
                repayActualInterest = investRepayModel.getRepayAmount();
                totalAmount += repayActualInterest;
            } else {
                repayExpectedInterest = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
                totalAmount += repayExpectedInterest;
            }

            List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(repayCalendarRequestDto.getBaseParam().getUserId(), investRepayModel.getInvestId(), null, null, repayCalendarRequestDto.getDate());
            for (CouponRepayModel couponRepayModel : couponRepayModelList) {
                if (couponRepayModel.getActualRepayDate() != null && couponRepayModel.getRepayAmount() == 0) {
                    continue;
                }
                if (couponRepayModel.getActualRepayDate() != null) {
                    repayActualInterest += couponRepayModel.getRepayAmount();
                    totalAmount += couponRepayModel.getRepayAmount();
                } else {
                    repayExpectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    totalAmount += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                }
            }
            int periods = investRepayMapper.findByInvestIdAndPeriodAsc(investRepayModel.getInvestId()).size();
            repayCalendarDateResponseDtoList.add(new RepayCalendarDateResponseDto(loanMapper.findById(investMapper.findById(investRepayModel.getInvestId()).getLoanId()).getName(),
                    AmountConverter.convertCentToString(repayActualInterest),
                    AmountConverter.convertCentToString(repayExpectedInterest),
                    investRepayModel.getActualRepayDate() != null && investRepayModel.getRepayAmount() > 0 ? String.valueOf(periods) : String.valueOf(investRepayModel.getPeriod()),
                    String.valueOf(periods),
                    investRepayModel.getStatus().name()));
        }

        List<TransferApplicationModel>  transferApplicationModels = transferApplicationMapper.findByInvestIdAndTransferTime(repayCalendarRequestDto.getBaseParam().getUserId(),null,null,repayCalendarRequestDto.getDate());
        for(TransferApplicationModel transferApplicationModel : transferApplicationModels){
            totalAmount += transferApplicationModel.getTransferAmount();
            int periods = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getTransferInvestId()).size();
            repayCalendarDateResponseDtoList.add(new RepayCalendarDateResponseDto(loanMapper.findById(transferApplicationModel.getLoanId()).getName(),
                    AmountConverter.convertCentToString(repayActualInterest),
                    AmountConverter.convertCentToString(repayExpectedInterest),
                    String.valueOf(periods),
                    String.valueOf(periods),
                    RepayStatus.COMPLETE.name()));
        }

        RepayCalendarDateListResponseDto repayCalendarDateListResponseDto = new RepayCalendarDateListResponseDto();
        repayCalendarDateListResponseDto.setTotalAmount(AmountConverter.convertCentToString(totalAmount));
        repayCalendarDateListResponseDto.setRepayCalendarDateResponseDtoList(repayCalendarDateResponseDtoList);
        baseResponseDto.setData(repayCalendarDateListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    private List<RepayCalendarYearResponseDto> getRepayCalendarResponseList(RepayCalendarRequestDto repayCalendarRequestDto, String type) {
        DateFormat sdf;
        if (type.equals(YEAR_REPAY_CALENDAR)) {
            sdf = new SimpleDateFormat("MM");
        } else {
            sdf = new SimpleDateFormat("dd");
        }

        List<InvestRepayModel> investRepayModelList = investRepayMapper.findInvestRepayByLoginNameAndRepayTime(repayCalendarRequestDto.getBaseParam().getUserId(), repayCalendarRequestDto.getYear(), replenishMonth(repayCalendarRequestDto.getMonth()), null);
        RepayCalendarYearResponseDto repayCalendarYearResponseDto;
        Map<String, RepayCalendarYearResponseDto> repayCalendarResponseDtoMaps = Maps.newConcurrentMap();
        for (InvestRepayModel investRepayModel : investRepayModelList) {
            List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investRepayModel.getInvestId(),Lists.newArrayList(TransferStatus.SUCCESS));
            if(CollectionUtils.isNotEmpty(transferApplicationModels)){
                repayCalendarResponseDtoMaps.put(sdf.format(transferApplicationModels.get(0).getTransferTime()), new RepayCalendarYearResponseDto(sdf.format(transferApplicationModels.get(0).getTransferTime()), transferApplicationModels.get(0).getTransferAmount()));
                continue;
            }
            if (investRepayModel.getActualRepayDate() != null && investRepayModel.getRepayAmount() == 0) {
                continue;
            }
            if (investRepayModel.getActualRepayDate() != null && repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getActualRepayDate())) == null) {
                repayCalendarResponseDtoMaps.put(sdf.format(investRepayModel.getActualRepayDate()), new RepayCalendarYearResponseDto(sdf.format(investRepayModel.getActualRepayDate()), investRepayModel));
                continue;
            } else if (investRepayModel.getActualRepayDate() == null && repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getRepayDate())) == null) {
                repayCalendarResponseDtoMaps.put(sdf.format(investRepayModel.getRepayDate()), new RepayCalendarYearResponseDto(sdf.format(investRepayModel.getRepayDate()), investRepayModel));
                continue;
            }

            if (investRepayModel.getActualRepayDate() != null) {
                repayCalendarYearResponseDto = repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getActualRepayDate()));
                repayCalendarYearResponseDto.setRepayAmount(addMoney(repayCalendarYearResponseDto.getRepayAmount(), String.valueOf(investRepayModel.getRepayAmount())));
            } else {
                repayCalendarYearResponseDto = repayCalendarResponseDtoMaps.get(sdf.format(investRepayModel.getRepayDate()));
                repayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(repayCalendarYearResponseDto.getExpectedRepayAmount(), String.valueOf(investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest())));
            }
        }

        Map<String, RepayCalendarYearResponseDto> couponRepayCalendarResponseDtoMaps = Maps.newConcurrentMap();
        RepayCalendarYearResponseDto couponRepayCalendarYearResponseDto;
        List<CouponRepayModel> couponRepayModelList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(repayCalendarRequestDto.getBaseParam().getUserId(), 0l, repayCalendarRequestDto.getYear(), replenishMonth(repayCalendarRequestDto.getMonth()), null);
        for (CouponRepayModel couponRepayModel : couponRepayModelList) {
            if (couponRepayModel.getActualRepayDate() != null && couponRepayModel.getRepayAmount() == 0) {
                continue;
            }
            if (couponRepayModel.getActualRepayDate() != null && couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getActualRepayDate())) == null) {
                couponRepayCalendarResponseDtoMaps.put(sdf.format(couponRepayModel.getActualRepayDate()), new RepayCalendarYearResponseDto(sdf.format(couponRepayModel.getActualRepayDate()), couponRepayModel));
                continue;
            } else if (couponRepayModel.getActualRepayDate() == null && couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getRepayDate())) == null) {
                couponRepayCalendarResponseDtoMaps.put(sdf.format(couponRepayModel.getRepayDate()), new RepayCalendarYearResponseDto(sdf.format(couponRepayModel.getRepayDate()), couponRepayModel));
                continue;
            }


            if (couponRepayModel.getActualRepayDate() != null) {
                couponRepayCalendarYearResponseDto = couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getActualRepayDate()));
                couponRepayCalendarYearResponseDto.setRepayAmount(addMoney(couponRepayCalendarYearResponseDto.getRepayAmount(), String.valueOf(couponRepayModel.getRepayAmount())));
            } else {
                couponRepayCalendarYearResponseDto = couponRepayCalendarResponseDtoMaps.get(sdf.format(couponRepayModel.getRepayDate()));
                couponRepayCalendarYearResponseDto.setExpectedRepayAmount(addMoney(couponRepayCalendarYearResponseDto.getExpectedRepayAmount(), String.valueOf(couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee())));
            }
        }

        Map<String,RepayCalendarYearResponseDto> transferRepayCalendarMaps = Maps.newConcurrentMap();

        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = Lists.newArrayList();
        for (String key : repayCalendarResponseDtoMaps.keySet()) {
            RepayCalendarYearResponseDto investRepay = repayCalendarResponseDtoMaps.get(key);
            RepayCalendarYearResponseDto couponRepay = couponRepayCalendarResponseDtoMaps.get(key);
            if (couponRepay != null) {
                investRepay.setRepayAmount(addMoney(investRepay.getRepayAmount(), couponRepay.getRepayAmount()));
                investRepay.setExpectedRepayAmount(addMoney(investRepay.getExpectedRepayAmount(), couponRepay.getExpectedRepayAmount()));
            }
            repayCalendarYearResponseDtoList.add(investRepay);
        }

        return repayCalendarYearResponseDtoList;
    }

    private String addMoney(String num1, String num2) {
        return String.valueOf(Long.parseLong(Strings.isNullOrEmpty(num1) ? "0" : num1) + Long.parseLong(Strings.isNullOrEmpty(num2) ? "0" : num2));
    }

    private String replenishMonth(String month) {
        if (Strings.isNullOrEmpty(month)) return null;
        return month.length() == 1 ? "0" + month : month;
    }
}
