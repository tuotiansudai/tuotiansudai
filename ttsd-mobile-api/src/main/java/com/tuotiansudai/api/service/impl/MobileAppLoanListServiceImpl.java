package com.tuotiansudai.api.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppLoanListService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppLoanListServiceImpl implements MobileAppLoanListService {

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;


    @Override
    public BaseResponseDto generateLoanList(LoanListRequestDto loanListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = loanListRequestDto.getIndex();
        Integer pageSize = loanListRequestDto.getPageSize();
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        index = (loanListRequestDto.getIndex() - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanListWeb(null, null, 0, 0, 0,
                0, index);
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(loanModels)) {
            LoanModel loanModel = loanModels.get(0);
            if(!ActivityType.NEWBIE.equals(loanModel.getActivityType())){
                LoanModel loanModelTemp  = loanMapper.getCompletedXsInvest();
                if(loanModelTemp != null){
                    loanModels.add(0, loanModelTemp);
                }
            }
            loanDtoList = convertLoanDto(loanModels);
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage("");
        if(CollectionUtils.isNotEmpty(loanDtoList)){

            LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
            loanListResponseDataDto.setLoanList(loanDtoList);
            loanListResponseDataDto.setIndex(loanListRequestDto.getIndex());
            loanListResponseDataDto.setPageSize(loanListRequestDto.getPageSize());
            loanListResponseDataDto.setTotalCount(loanMapper.findLoanListCountWeb(null,null,0,0,0,0));
            dto.setData(loanListResponseDataDto);
        }
        return dto;
    }


    private List<LoanResponseDataDto> convertLoanDto(List<LoanModel> loanList) {
        List<LoanResponseDataDto> loanDtoList = new ArrayList<LoanResponseDataDto>();
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            loanResponseDataDto.setLoanType(loan.getActivityType().name());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setRepayTypeCode("");
            loanResponseDataDto.setRepayTypeName(loan.getType().getName());
            loanResponseDataDto.setDeadline(loan.getPeriods());
            loanResponseDataDto.setRepayUnit(loan.getType().getLoanPeriodUnit().getDesc());
            loanResponseDataDto.setRatePercent(decimalFormat.format((loan.getBaseRate() + loan.getActivityRate()) * 100));
            loanResponseDataDto.setLoanMoney(AmountConverter.convertCentToString(loan.getLoanAmount()));
            loanResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
            loanResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
            loanResponseDataDto.setMinInvestMoney(AmountConverter.convertCentToString(loan.getMinInvestAmount()));
            loanResponseDataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loan.getMaxInvestAmount()));
            loanResponseDataDto.setCardinalNumber(AmountConverter.convertCentToString(loan.getInvestIncreasingAmount()));
            if(loan.getFundraisingStartTime() != null){
                loanResponseDataDto.setInvestBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getFundraisingStartTime()));
            }
            loanResponseDataDto.setInvestBeginSeconds(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
            long investedAmount = investMapper.sumSuccessInvestAmount(loan.getId());
            loanResponseDataDto.setInvestedMoney(AmountConverter.convertCentToString(investedAmount));
            loanResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
            loanResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));

            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }

 }
