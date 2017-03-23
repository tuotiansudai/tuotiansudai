package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;

@Service
public class MobileAppTransferServiceImpl implements MobileAppTransferService {

    static Logger logger = Logger.getLogger(MobileAppTransferServiceImpl.class);

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferService transferService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Value("${pay.callback.app.web.host}")
    private String domainName;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private PageValidUtils pageValidUtils;

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
    public BaseResponseDto<InvestNoPassResponseDataDto> transferNoPasswordPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto) {
        try {
            InvestDto investDto = convertInvestDto(transferPurchaseRequestDto);
            investDto.setNoPassword(true);
            BaseDto<PayDataDto> payDataDto = transferService.noPasswordTransferPurchase(investDto);

            if (payDataDto.getData().getStatus()) {
                BaseResponseDto<InvestNoPassResponseDataDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                InvestModel latestSuccessInvest = investMapper.findLatestSuccessInvest(investDto.getLoginName());
                String url = MessageFormat.format("{0}/{1}?order_id={2}", domainName, AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD.getMobileRetCallbackPath(), String.valueOf(latestSuccessInvest.getId()));
                baseResponseDto.getData().setUrl(url);
            }

            return new BaseResponseDto<>(payDataDto.getData().getCode(), payDataDto.getData().getMessage());
        } catch (InvestException e) {
            return new BaseResponseDto<>(this.convertExceptionToDto(e));
        }
    }

    @Override
    public BaseResponseDto<InvestResponseDataDto> transferPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto) {
        BaseResponseDto<InvestResponseDataDto> responseDto = new BaseResponseDto<>();
        InvestDto investDto = convertInvestDto(transferPurchaseRequestDto);
        try {
            BaseDto<PayFormDataDto> formDto = transferService.transferPurchase(investDto);
            if (!formDto.isSuccess()) {
                logger.error("[MobileAppTransferServiceImpl][transferPurchase] pay wrapper may fail to connect.");

                responseDto.setCode(ReturnMessage.FAIL.getCode());
                responseDto.setMessage(ReturnMessage.FAIL.getMsg());
                return responseDto;
            }
            if (formDto.getData().getStatus()) {
                PayFormDataDto formDataDto = formDto.getData();
                String requestData = CommonUtils.mapToFormData(formDataDto.getFields());

                InvestResponseDataDto investResponseDataDto = new InvestResponseDataDto();
                investResponseDataDto.setRequestData(requestData);
                investResponseDataDto.setUrl(formDataDto.getUrl());
                responseDto.setCode(ReturnMessage.SUCCESS.getCode());
                responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                responseDto.setData(investResponseDataDto);
            } else {
                responseDto.setCode(ReturnMessage.INVEST_FAILED.getCode());
                responseDto.setMessage(ReturnMessage.INVEST_FAILED.getMsg() + ":" + formDto.getData().getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            responseDto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
            responseDto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        } catch (InvestException e) {
            return new BaseResponseDto<>(this.convertExceptionToDto(e));
        }
        return responseDto;
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
        investDto.setTransferInvestId(transferPurchaseRequestDto.getTransferApplicationId());
        return investDto;
    }

    private ReturnMessage convertExceptionToDto(InvestException e) {
        switch (e.getType()) {
            case ILLEGAL_LOAN_STATUS:
                return ReturnMessage.ILLEGAL_LOAN_STATUS;
            case NOT_ENOUGH_BALANCE:
                return ReturnMessage.NOT_ENOUGH_BALANCE;
            case PASSWORD_INVEST_OFF:
                return ReturnMessage.PASSWORD_INVEST_OFF;
            case LOAN_NOT_FOUND:
                return ReturnMessage.LOAN_NOT_FOUND;
            case INVESTOR_IS_LOANER:
                return ReturnMessage.APPLICATION_IS_HIS_OWN;
        }
        return ReturnMessage.ERROR;
    }

}
