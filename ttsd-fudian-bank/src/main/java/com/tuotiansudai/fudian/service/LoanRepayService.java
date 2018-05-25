package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanInvestStatus;
import com.tuotiansudai.fudian.dto.request.LoanRepayRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanRepayContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.ReturnUpdateMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanRepayService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanRepayService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final BankClient bankClient;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public LoanRepayService(SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public LoanRepayRequestDto repay(Source source, String loginName, String mobile, String userName, String accountNo, String loanTxNo, String capital, String interest) {
        LoanRepayRequestDto dto = new LoanRepayRequestDto(source, loginName, mobile, userName, accountNo, loanTxNo, capital, interest, ApiType.LOAN_REPAY, null);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan repay] sign error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        this.insertMapper.insertLoanRepay(dto);
        return dto;
    }

    public ResponseDto fastRepay(Source source, String loginName, String mobile, String userName, String accountNo, String loanTxNo, String capital, String interest) {
        LoanRepayRequestDto dto = new LoanRepayRequestDto(source, loginName, mobile, userName, accountNo, loanTxNo, capital, interest, ApiType.LOAN_FAST_REPAY, null);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan fast repay] sign error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        insertMapper.insertLoanRepay(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FAST_REPAY);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan fast repay] send error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan fast repay] verify sign error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_FAST_REPAY.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan fast repay] parse response error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        this.updateMapper.updateLoanRepay(responseDto);
        return responseDto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateLoanRepay(responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[loan repay callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.LOAN_REPAY.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan repay callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateLoanRepay(responseDto);

        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_REPAY.name(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanRepayContentDto> responseDto = (ResponseDto<LoanRepayContentDto>) ApiType.LOAN_REPAY.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
