package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.request.RechargePayType;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.ReturnUpdateMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechargeService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RechargeService.class);

    private final BankConfig bankConfig;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public RechargeService(BankConfig bankConfig, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.bankConfig = bankConfig;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public RechargeRequestDto recharge(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, String amount, RechargePayType payType) {
        RechargeRequestDto dto = new RechargeRequestDto(source, loginName, mobile, bankUserName, bankAccountNo, amount, payType, null);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[recharge] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}", bankUserName, bankAccountNo, amount, payType);
            return null;
        }
        insertMapper.insertRecharge(dto);
        return dto;

    }

    public RechargeRequestDto merchantRecharge(Source source, String loginName, String mobile, String amount) {
        RechargeRequestDto dto = new RechargeRequestDto(source, loginName, mobile, bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY, null);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant recharge sign] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}",
                    bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);
            return null;
        }

        insertMapper.insertRecharge(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateRecharge(responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[recharge callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.RECHARGE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[recharge callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateRecharge(responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.RECHARGE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<RechargeContentDto> responseDto = (ResponseDto<RechargeContentDto>) ApiType.RECHARGE.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
