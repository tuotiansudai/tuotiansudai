package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BasePaginationDataDto;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferApplicationService;

import com.tuotiansudai.dto.TransferApplicationDetailDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private InvestService investService;
    @Autowired
    private LoanRepayMapper loanRepayMapper;


    @Override
    public BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        String loginName = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
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
        if (CollectionUtils.isNotEmpty(transferApplicationRecordDtos)) {
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
            boolean result = investTransferService.investTransferApply(transferApplicationDto);
            if (!result) {
                return new BaseResponseDto(ReturnMessage.TRANSFER_APPLY_IS_FAIL.getCode(), ReturnMessage.TRANSFER_APPLY_IS_FAIL.getMsg());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new BaseResponseDto(ReturnMessage.TRANSFER_APPLY_IS_FAIL.getCode(), ReturnMessage.TRANSFER_APPLY_IS_FAIL.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    @Override
    public BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto) {
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        String investId = requestDto.getInvestId();

        if (!investTransferService.isTransferable(Long.parseLong(investId))) {
            return new BaseResponseDto(ReturnMessage.TRANSFER_IS_NOT_EXIST.getCode(), ReturnMessage.TRANSFER_IS_NOT_EXIST.getMsg());
        }
        InvestModel investModel = investMapper.findById(Long.parseLong(investId));
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        TransferApplyQueryResponseDataDto transferApplyQueryResponseDataDto = new TransferApplyQueryResponseDataDto();

        transferApplyQueryResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
        transferApplyQueryResponseDataDto.setDeadLine(new DateTime(investTransferService.getDeadlineFromNow()).toString("yyyy/MM/dd HH:mm:ss"));

        BigDecimal investAmountBig = new BigDecimal(investModel.getAmount());
        BigDecimal discountBig = new BigDecimal(transferRuleModel.getDiscount());

        long discountLower = investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
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
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }

        List<TransferApplicationRecordDto> transferApplicationRecordDtos = transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(loginName,
                (index - 1) * pageSize,
                pageSize);
        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(loginName));
        if (CollectionUtils.isNotEmpty(transferApplicationRecordDtos)) {
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
        transferPurchaseResponseDataDto.setBalance(AmountConverter.convertCentToString((accountMapper.findByLoginName(requestDto.getBaseParam().getUserId()).getBalance())));
        transferPurchaseResponseDataDto.setTransferAmount(AmountConverter.convertCentToString((transferApplicationModel.getTransferAmount())));
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? transferApplicationModel.getInvestId():transferApplicationModel.getTransferInvestId());
        transferPurchaseResponseDataDto.setExpectedInterestAmount(AmountConverter.convertCentToString(InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels)));

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferPurchaseResponseDataDto);
        return dto;
    }

    public BaseResponseDto transferApplicationList(TransferApplicationListRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String rateLower = requestDto.getRateLower();
        String rateUpper = requestDto.getRateUpper();
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (rateLower == null || rateLower == "") {
            rateLower = "0";
        }
        if (rateUpper == null || rateUpper == "") {
            rateUpper = "0";
        }

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationRecordDto = transferService.findAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper), index, pageSize);

        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferService.findCountAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper)));

        if (CollectionUtils.isNotEmpty(transferApplicationRecordDto.getRecords())) {
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

    public BaseResponseDto transferApplicationById(TransferApplicationDetailRequestDto requestDto){
        BaseResponseDto<TransferApplicationDetailResponseDataDto> dto = new BaseResponseDto();
        String transferApplicationId = requestDto.getTransferApplicationId();
        String loginName = requestDto.getBaseParam().getUserId();
        TransferApplicationDetailDto transferApplicationDetailDto = transferService.getTransferApplicationDetailDto(Long.parseLong(transferApplicationId),loginName, 3);
        TransferApplicationDetailResponseDataDto transferApplicationDetailResponseDataDto = new TransferApplicationDetailResponseDataDto(transferApplicationDetailDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationDetailResponseDataDto);
        return dto;
    }
    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investList) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        Map<Long, LoanModel> loanMapCache = Maps.newHashMap();
        if (investList != null) {
            for (InvestModel invest : investList) {
                TransferRuleModel transferRuleModel =  transferRuleMapper.find();
                if(!transferRuleModel.isMultipleTransferEnabled()){
                    TransferApplicationModel transfereeApplicationModel = transferApplicationMapper.findByInvestId(invest.getId());
                    if( transfereeApplicationModel != null){
                        logger.debug(MessageFormat.format("{0} MultipleTransferEnabled is false ", invest.getId()));
                        continue;
                    }

                }
                long loanId = invest.getLoanId();
                LoanModel loanModel;
                if (loanMapCache.containsKey(loanId)) {
                    loanModel = loanMapCache.get(loanId);
                } else {
                    loanModel = loanMapper.findById(loanId);
                    loanMapCache.put(loanId, loanModel);
                }
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(invest, loanModel);

                long amount = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(invest.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                }

                if (org.apache.commons.collections4.CollectionUtils.isEmpty(investRepayModels)) {
                    amount = investService.estimateInvestIncome(invest.getLoanId(), invest.getAmount());
                }

                dto.setInvestInterest(AmountConverter.convertCentToString(amount));
                dto.setTransferStatus(invest.getTransferStatus().name());
                LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(invest.getLoanId());
                dto.setLeftPeriod(loanRepayModel == null ? "0" : String.valueOf(investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(invest.getId(), loanRepayModel.getPeriod())));
                list.add(dto);
            }
        }
        return list;

    }
}
