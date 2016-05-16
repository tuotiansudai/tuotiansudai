package com.tuotiansudai.api.service.v2_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("com.tuotiansudai.api.service.v2_0.impl.MobileAppLoanListServiceImpl")
public class MobileAppLoanListV2V2ServiceImpl implements MobileAppLoanListV2Service {

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;

    @Override
    public BaseResponseDto generateIndexLoan(BaseParamDto baseParamDto) {
        List<LoanModel> loanModels;
        if(investMapper.sumSuccessInvestCountByLoginName(baseParamDto.getBaseParam().getUserId()) > 0){
            loanModels = loanMapper.findHomeLoanByIsContainNewBie("true");
            if(CollectionUtils.isEmpty(loanModels)){

            }
        }else{
            loanModels = loanMapper.findHomeLoanByIsContainNewBie("false");
            if(CollectionUtils.isNotEmpty(loanModels)){
            }
        }

        BaseResponseDto dto = new BaseResponseDto();
        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();

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
        List<LoanResponseDataDto> loanDtoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setActivityType(loan.getActivityType().name());
            loanResponseDataDto.setDuration(String.valueOf(loan.getDuration()));
            loanResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
            loanResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));
            loanResponseDataDto.setLoanAmount(AmountConverter.convertCentToString(loan.getLoanAmount()));
            loanResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmount(loan.getId())));
            if(com.tuotiansudai.repository.model.LoanStatus.PREHEAT.equals(loan.getStatus())){
                loanResponseDataDto.setLoanStatus(com.tuotiansudai.repository.model.LoanStatus.RAISING.name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(com.tuotiansudai.repository.model.LoanStatus.RAISING.getDescription());
            }else{
                loanResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
            }
            loanResponseDataDto.setFundraisingStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getFundraisingStartTime()));
            loanResponseDataDto.setFundraisingCountDown(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }
}
