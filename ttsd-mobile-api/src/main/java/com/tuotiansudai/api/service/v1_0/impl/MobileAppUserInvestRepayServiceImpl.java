package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserInvestRepayService;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppUserInvestRepayServiceImpl implements MobileAppUserInvestRepayService {

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Override
    public BaseResponseDto userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = new BaseResponseDto<>();
        long totalExpectedInterest = 0;
        long completeTotalActualInterest = 0;
        int periodCount = 0;

        try {
            InvestModel investModel = investService.findById(Long.parseLong(userInvestRepayRequestDto.getInvestId().trim()));
            LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
            //未放款时按照预计利息计算
            if(loanModel.getRecheckTime() == null){
                totalExpectedInterest = InterestCalculator.estimateExpectedInterest(loanModel, investModel.getAmount());
            }
            UserInvestRepayResponseDataDto userInvestRepayResponseDataDto = new UserInvestRepayResponseDataDto(loanModel, investModel);
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            List<InvestRepayDataDto> investRepayList = new ArrayList<>();

            for (InvestRepayModel investRepayModel : investRepayModels) {
                InvestRepayDataDto investRepayDataDto = new InvestRepayDataDto();
                investRepayDataDto.setPeriod(investRepayModel.getPeriod());
                investRepayDataDto.setRepayDate(sdf.format(investRepayModel.getRepayDate()));
                investRepayDataDto.setActualRepayDate(investRepayModel.getActualRepayDate() == null ? "" : sdf.format(investRepayModel.getActualRepayDate()));
                investRepayDataDto.setExpectedInterest(AmountConverter.convertCentToString(investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee()));
                investRepayDataDto.setActualInterest(AmountConverter.convertCentToString(investRepayModel.getRepayAmount()));
                investRepayDataDto.setStatus(investRepayModel.getStatus().name());
                investRepayList.add(investRepayDataDto);
                if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                    completeTotalActualInterest += investRepayModel.getRepayAmount();
                }
                totalExpectedInterest += (investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee());
                periodCount++;
                //用最后一次待还款日期做为到期还款日
                if(periodCount == investRepayModels.size()){
                    userInvestRepayResponseDataDto.setLastRepayDate(investRepayModel.getRepayDate() != null?sdf.format(investRepayModel.getRepayDate()):"");
                }
            }
            userInvestRepayResponseDataDto.setExpectedInterest(AmountConverter.convertCentToString(totalExpectedInterest));
            userInvestRepayResponseDataDto.setActualInterest(AmountConverter.convertCentToString(completeTotalActualInterest));
            userInvestRepayResponseDataDto.setInvestRepays(investRepayList);

            responseDto.setCode(ReturnMessage.SUCCESS.getCode());
            responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            responseDto.setData(userInvestRepayResponseDataDto);
        }catch(Exception e){
            responseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            responseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        return responseDto;
    }
}
