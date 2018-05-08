package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanInvestService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanInvestService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final BankClient bankClient;

    @Autowired
    public LoanInvestService(SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public LoanInvestRequestDto invest(String loginName, String mobile, String userName, String accountNo, String amount, String award, String loanTxNo) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(loginName, mobile, userName, accountNo, amount, award, loanTxNo, ApiType.LOAN_INVEST);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan invest] sign error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        insertMapper.insertLoanInvest(dto);
        return dto;
    }

    public ResponseDto fastInvest(String loginName, String mobile, String userName, String accountNo, String amount, String award, String loanTxNo) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(loginName, mobile, userName, accountNo, amount, award, loanTxNo, ApiType.LOAN_FAST_INVEST);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan fast invest] sign error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        insertMapper.insertLoanInvest(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FAST_INVEST);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan fast invest] send error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan fast invest] verify sign error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_FAST_INVEST.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan fast invest] parse response error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        this.updateMapper.updateLoanInvest(responseDto);
        return responseDto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[loan invest callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.LOAN_INVEST.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan invest callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateLoanInvest(responseDto);

        return responseDto;
    }
}
