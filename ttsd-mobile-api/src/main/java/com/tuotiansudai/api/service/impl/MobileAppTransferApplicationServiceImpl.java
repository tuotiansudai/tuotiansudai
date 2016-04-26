package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MobileAppTransferApplicationServiceImpl implements MobileAppTransferApplicationService {

    static Logger logger = Logger.getLogger(MobileAppTransferApplicationServiceImpl.class);
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
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
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;


    @Override
    public BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        String loginName = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if(index == null || index <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }

        List<TransferApplicationRecordDto> transferApplicationRecordDtos = transferApplicationMapper.findTransferApplicationPaginationByLoginName(loginName,
                transferStatusList,
                (index - 1) * pageSize,
                pageSize);
        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(loginName, transferStatusList));
        if(CollectionUtils.isNotEmpty(transferApplicationRecordDtos)){
            List<TransferApplicationRecordResponseDataDto> transferApplication = Lists.transform(transferApplicationRecordDtos, new Function<TransferApplicationRecordDto, TransferApplicationRecordResponseDataDto>() {
                @Override
                public TransferApplicationRecordResponseDataDto apply(TransferApplicationRecordDto transferApplicationRecordDto) {
                    return new TransferApplicationRecordResponseDataDto(transferApplicationRecordDto);
                }
            });
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);

        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto transferApply(TransferApplyRequestDto requestDto) {
        TransferApplicationDto transferApplicationDto = requestDto.convertToTransferApplicationDto();
        TransferRuleModel transferRuleModel =  transferRuleMapper.find();
        InvestModel investModel = investMapper.findById(transferApplicationDto.getTransferInvestId());
        BigDecimal investAmountBig = new BigDecimal(investModel.getAmount());
        BigDecimal discountBig = new BigDecimal(transferRuleModel.getDiscount());
        long transferAmount = AmountConverter.convertStringToCent(requestDto.getTransferAmount());
        long discountLower =  investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0,BigDecimal.ROUND_DOWN).longValue();
        long discountUpper = investModel.getAmount();
        if(transferAmount > discountUpper || transferAmount < discountLower){
            return new BaseResponseDto(ReturnMessage.TRANSFER_AMOUNT_OUT_OF_RANGE.getCode(),ReturnMessage.TRANSFER_AMOUNT_OUT_OF_RANGE.getMsg());
        }

        try {
            investTransferService.investTransferApply(transferApplicationDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
            return new BaseResponseDto(ReturnMessage.TRANSFER_APPLY_IS_FAIL.getCode(),ReturnMessage.TRANSFER_APPLY_IS_FAIL.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
    }

    @Override
    public BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto) {
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        String investId = requestDto.getInvestId();

        if(!investTransferService.isTransferable(Long.parseLong(investId))){
            return new BaseResponseDto(ReturnMessage.TRANSFER_IS_NOT_EXIST.getCode(),ReturnMessage.TRANSFER_IS_NOT_EXIST.getMsg());
        }
        InvestModel investModel = investMapper.findById(Long.parseLong(investId));
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        TransferRuleModel transferRuleModel =  transferRuleMapper.find();
        TransferApplyQueryResponseDataDto transferApplyQueryResponseDataDto = new TransferApplyQueryResponseDataDto();

        transferApplyQueryResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
        transferApplyQueryResponseDataDto.setDeadLine(new DateTime(investTransferService.getDeadlineFromNow()).toString("yyyy/MM/dd HH:mm:ss"));

        BigDecimal investAmountBig = new BigDecimal(investModel.getAmount());
        BigDecimal discountBig = new BigDecimal(transferRuleModel.getDiscount());

        long discountLower =  investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0,BigDecimal.ROUND_DOWN).longValue();
        transferApplyQueryResponseDataDto.setDiscountLower(AmountConverter.convertCentToString(discountLower));
        transferApplyQueryResponseDataDto.setDiscountUpper(transferApplyQueryResponseDataDto.getInvestAmount());
        transferApplyQueryResponseDataDto.setTransferFee(AmountConverter.convertCentToString(TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel)));

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(transferApplyQueryResponseDataDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto generateTransfereeApplication(PaginationRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String loginName = requestDto.getBaseParam().getUserId();
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        if(index == null || index <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }

        List<TransferApplicationRecordDto> transferApplicationRecordDtos = transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(loginName,
                (index - 1) * pageSize,
                pageSize);
        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(loginName));
        if(CollectionUtils.isNotEmpty(transferApplicationRecordDtos)){
            List<TransferApplicationRecordResponseDataDto> transferApplication = Lists.transform(transferApplicationRecordDtos, new Function<TransferApplicationRecordDto, TransferApplicationRecordResponseDataDto>() {
                @Override
                public TransferApplicationRecordResponseDataDto apply(TransferApplicationRecordDto transferApplicationRecordDto) {
                    return new TransferApplicationRecordResponseDataDto(transferApplicationRecordDto);
                }
            });
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);

        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto transferApplicationCancel(TransferCancelRequestDto transferCancelRequestDto) {
        boolean isSuccess = investTransferService.cancelTransferApplication(transferCancelRequestDto.getTransferApplicationId());
        if (isSuccess) {
            return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        } else {
            return new BaseResponseDto(ReturnMessage.TRANSFER_CANCEL_IS_FAIL.getCode(), ReturnMessage.TRANSFER_CANCEL_IS_FAIL.getMsg());
        }
    }

    @Override
    public BaseResponseDto transferPurchase(TransferPurchaseRequestDto requestDto){
        BaseResponseDto<TransferPurchaseResponseDataDto> dto = new BaseResponseDto();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(requestDto.getTransferApplicationId()));

        TransferPurchaseResponseDataDto transferPurchaseResponseDataDto = new TransferPurchaseResponseDataDto();
        transferPurchaseResponseDataDto.setBalance(AmountConverter.convertCentToString((accountMapper.findByLoginName(transferApplicationModel.getLoginName()).getBalance())));
        transferPurchaseResponseDataDto.setTransferAmount(AmountConverter.convertCentToString((transferApplicationModel.getTransferAmount())));

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getInvestId());
        long totalExpectedInterestAmount = 0;
        for(int i = transferApplicationModel.getPeriod() - 1  ; i < investRepayModels.size(); i ++ ){
            totalExpectedInterestAmount +=  investRepayModels.get(i).getCorpus() + investRepayModels.get(i).getExpectedInterest() - investRepayModels.get(i).getExpectedFee();
        }
        transferPurchaseResponseDataDto.setExpectedInterestAmount(AmountConverter.convertCentToString(totalExpectedInterestAmount));

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferPurchaseResponseDataDto);
        return dto;
    }

}
