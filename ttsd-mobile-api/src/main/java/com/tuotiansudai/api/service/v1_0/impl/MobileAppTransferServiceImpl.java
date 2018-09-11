package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MobileAppTransferServiceImpl implements MobileAppTransferService {

    static Logger logger = Logger.getLogger(MobileAppTransferServiceImpl.class);

    private final TransferApplicationMapper transferApplicationMapper;

    private final InvestMapper investMapper;

    private final TransferService transferService;

    private final MobileAppChannelService mobileAppChannelService;

    private final RandomUtils randomUtils;

    private final PageValidUtils pageValidUtils;

    @Autowired
    public MobileAppTransferServiceImpl(TransferApplicationMapper transferApplicationMapper, InvestMapper investMapper, TransferService transferService, MobileAppChannelService mobileAppChannelService, RandomUtils randomUtils, PageValidUtils pageValidUtils){
        this.transferApplicationMapper = transferApplicationMapper;
        this.investMapper = investMapper;
        this.transferService = transferService;
        this.mobileAppChannelService = mobileAppChannelService;
        this.randomUtils = randomUtils;
        this.pageValidUtils = pageValidUtils;
    }

    @Override
    public BaseResponseDto<TransferTransfereeResponseDataDto> getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto) {
        BaseResponseDto<TransferTransfereeResponseDataDto> dto = new BaseResponseDto<>();
        Integer index = transferTransfereeRequestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(transferTransfereeRequestDto.getPageSize());
        if (index == null || index <= 0 || pageSize <= 0) {
            return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferTransfereeRequestDto.getTransferApplicationId());
        InvestModel investModel = null;
        if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS && transferApplicationModel.getInvestId() != null) {
            investModel = investMapper.findById(transferApplicationModel.getInvestId());
        }
        TransferTransfereeRecordResponseDataDto transferTransfereeRecordResponseDataDto = new TransferTransfereeRecordResponseDataDto(investModel != null ? randomUtils.encryptMobile(transferTransfereeRequestDto.getBaseParam().getUserId(), investModel.getLoginName(), investModel.getId()) : "",
                transferApplicationModel.getTransferAmount(), transferApplicationModel.getTransferTime());
        TransferTransfereeResponseDataDto transferTransfereeResponseDataDto = new TransferTransfereeResponseDataDto(transferTransfereeRequestDto.getIndex(), transferTransfereeRequestDto.getPageSize(),
                Lists.newArrayList(transferTransfereeRecordResponseDataDto).size(), Lists.newArrayList(transferTransfereeRecordResponseDataDto));
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferTransfereeResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> transferPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto) {
        InvestDto investDto = convertInvestDto(transferPurchaseRequestDto);
        try {
            BankAsyncMessage bankAsyncMessage = transferService.transferPurchase(investDto);
            return CommonUtils.mapToFormData(bankAsyncMessage);
        } catch (InvestException e) {
            return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED.getCode(), e.getType().getDescription());
        }
    }

    private InvestDto convertInvestDto(TransferPurchaseRequestDto transferPurchaseRequestDto) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(transferPurchaseRequestDto.getTransferApplicationId()));
        Source source = Source.valueOf(transferPurchaseRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH));
        InvestDto investDto = new InvestDto();
        investDto.setSource(source);
        investDto.setAmount(String.valueOf(transferApplicationModel.getTransferAmount()));
        investDto.setLoanId(String.valueOf(transferApplicationModel.getLoanId()));
        investDto.setLoginName(transferPurchaseRequestDto.getBaseParam().getUserId());
        investDto.setChannel(mobileAppChannelService.obtainChannelBySource(transferPurchaseRequestDto.getBaseParam()));
        investDto.setTransferApplicationId(transferPurchaseRequestDto.getTransferApplicationId());
        return investDto;
    }
}
