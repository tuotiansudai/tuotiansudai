package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;
import com.tuotiansudai.api.dto.TransferApplyQueryResponseDataDto;
import com.tuotiansudai.api.service.MobileAppTransferApplyQueryService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MobileAppTransferApplyQueryServiceImpl implements MobileAppTransferApplyQueryService {

    static Logger logger = Logger.getLogger(MobileAppTransferApplyQueryServiceImpl.class);
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private InvestTransferService investTransferService;
    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Override
    public BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto) {
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        String investId = requestDto.getInvestId();

        if(!investTransferService.isTransfer(Long.parseLong(investId))){
            return new BaseResponseDto(ReturnMessage.TRANSFER_IS_NOT_EXIST.getCode(),ReturnMessage.TRANSFER_IS_NOT_EXIST.getMsg());
        }
        InvestModel investModel = investMapper.findById(Long.parseLong(investId));
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(investModel.getLoanId());
        DateTime currentRepayDate = new DateTime();
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, currentRepayDate);
        int periodDuration = Days.daysBetween(lastRepayDate.withTimeAtStartOfDay(), currentRepayDate.withTimeAtStartOfDay()).getDays();
        TransferApplyQueryResponseDataDto transferApplyQueryResponseDataDto = new TransferApplyQueryResponseDataDto();

        transferApplyQueryResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
        transferApplyQueryResponseDataDto.setTransferInterestDays(String.valueOf(periodDuration));
        transferApplyQueryResponseDataDto.setTransferInterest(AmountConverter.convertCentToString(InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate)));
        TransferRuleModel transferRuleModel =  transferRuleMapper.find();
        transferApplyQueryResponseDataDto.setTransferFee(AmountConverter.convertCentToString(TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel)));

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(transferApplyQueryResponseDataDto);
        return baseResponseDto;
    }
}
