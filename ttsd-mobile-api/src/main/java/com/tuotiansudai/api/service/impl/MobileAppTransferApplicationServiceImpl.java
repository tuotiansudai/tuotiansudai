package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
    private InvestTransferService investTransferService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private TransferRuleMapper transferRuleMapper;

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

        long discountLower =  investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0).longValue();
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
    public BaseResponseDto transferApplicationList(TransferApplicationListRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String rateLower = requestDto.getRateLower();
        String rateUpper = requestDto.getRateUpper();
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if(index == null || index <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        if(rateLower == null || rateLower == ""){
            rateLower = "0";
        }
        if(rateUpper == null || rateUpper == ""){
            rateUpper = "0";
        }

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationRecordDto = transferService.findAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper), index, pageSize);

        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferService.findCountAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper)));

        if(CollectionUtils.isNotEmpty(transferApplicationRecordDto.getRecords())){
            List<TransferApplicationRecordResponseDataDto> transferApplication = Lists.transform(transferApplicationRecordDto.getRecords(), new Function<TransferApplicationPaginationItemDataDto, TransferApplicationRecordResponseDataDto>() {
                @Override
                public TransferApplicationRecordResponseDataDto apply(TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto) {
                    return new TransferApplicationRecordResponseDataDto(transferApplicationPaginationItemDataDto);
                }
            });
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);
        }

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }
}
