package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanListService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.ProductTypeConvertor;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
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
    public BaseResponseDto<LoanListResponseDataDto> generateLoanList(LoanListRequestDto loanListRequestDto) {
        BaseResponseDto<LoanListResponseDataDto> dto = new BaseResponseDto<>();
        Integer index = loanListRequestDto.getIndex();
        Integer pageSize = loanListRequestDto.getPageSize();
        if (index == null || pageSize == null || index <=0 || pageSize <=0) {
            return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        index = (loanListRequestDto.getIndex() - 1) * pageSize;
        List<LoanModel> loanModels = loanMapper.findLoanListWeb(loanListRequestDto.getProductType() != null ? ProductTypeConvertor.stringConvertTo(loanListRequestDto.getProductType()) : null, loanListRequestDto.getLoanStatus(),loanListRequestDto.getRateLower(),loanListRequestDto.getRateUpper(), index);
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(loanModels)) {
            loanDtoList = convertLoanDto(loanModels);
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());

        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
        loanListResponseDataDto.setIndex(loanListRequestDto.getIndex());
        loanListResponseDataDto.setPageSize(loanListRequestDto.getPageSize());
        loanListResponseDataDto.setTotalCount(loanMapper.findLoanListCountWeb(loanListRequestDto.getProductType() != null ? ProductTypeConvertor.stringConvertTo(loanListRequestDto.getProductType()) : null, loanListRequestDto.getLoanStatus(),loanListRequestDto.getRateLower(),loanListRequestDto.getRateUpper()));

        if(CollectionUtils.isNotEmpty(loanDtoList)){
            loanListResponseDataDto.setLoanList(loanDtoList);
            dto.setData(loanListResponseDataDto);
        } else {
            loanListResponseDataDto.setLoanList(new ArrayList<LoanResponseDataDto>());
            dto.setData(loanListResponseDataDto);
        }
        return dto;
    }

    @Override
    public BaseResponseDto generateIndexLoan(BaseParamDto baseParamDto) {
        BaseResponseDto dto = new BaseResponseDto();
        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
        List<LoanModel> loanModels = loanMapper.findHomeLoan();
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(loanModels)) {
            loanDtoList = convertLoanDto(loanModels);
            loanListResponseDataDto.setLoanList(loanDtoList);
            dto.setData(loanListResponseDataDto);
        }else{
            loanListResponseDataDto.setLoanList(new ArrayList<LoanResponseDataDto>());
            dto.setData(loanListResponseDataDto);
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }


    private List<LoanResponseDataDto> convertLoanDto(List<LoanModel> loanList) {
        List<LoanResponseDataDto> loanDtoList = new ArrayList<LoanResponseDataDto>();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            loanResponseDataDto.setLoanType(loan.getProductType() != null ? loan.getProductType().name() : "");
            loanResponseDataDto.setLoanTypeName(loan.getProductType() != null ? loan.getProductType().getName() : "");
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setRepayTypeCode("");
            loanResponseDataDto.setRepayTypeName(loan.getType().getName());
            loanResponseDataDto.setDeadline(loan.getPeriods());
            loanResponseDataDto.setRepayUnit(loan.getType().getLoanPeriodUnit().getDesc());
            loanResponseDataDto.setRatePercent(decimalFormat.format((loan.getBaseRate() + loan.getActivityRate()) * 100));
            loanResponseDataDto.setLoanMoney(AmountConverter.convertCentToString(loan.getLoanAmount()));
            if(LoanStatus.PREHEAT.equals(loan.getStatus())){
                loanResponseDataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(LoanStatus.RAISING.getDescription());
            }else{
                loanResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
            }
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
            loanResponseDataDto.setInvestFeeRate("" + loan.getInvestFeeRate());
            loanResponseDataDto.setDuration(String.valueOf(loan.getDuration()));
            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }


 }
