package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanFullRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanFullContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanFullService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanFullService.class);

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public LoanFullService(BankClient bankClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public ResponseDto full(String loginName, String mobile, String userName, String accountNo, String loanTxNo, String loanOrderNo, String loanOrderDate, String expectRepayTime) {
        LoanFullRequestDto dto = new LoanFullRequestDto(loginName, mobile, userName, accountNo, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime, null);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan full] sign error, userName: {}, accountNo: {}, loanTxNo: {}, loanOrderNo: {}, loanOrderDate: {}, expectRepayTime: {}",
                    userName, accountNo, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime);
            return null;
        }

        insertMapper.insertLoanFull(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FULL);
        if (responseData == null) {
            logger.error("[loan full] send error, userName: {}, accountNo: {}, loanTxNo: {}, loanOrderNo: {}, loanOrderDate: {}, expectRepayTime: {}",
                    userName, accountNo, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan full] verify sign error, userName: {}, accountNo: {}, loanTxNo: {}, loanOrderNo: {}, loanOrderDate: {}, expectRepayTime: {}",
                    userName, accountNo, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_FULL.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan full] parse response error, userName: {}, accountNo: {}, loanTxNo: {}, loanOrderNo: {}, loanOrderDate: {}, expectRepayTime: {}",
                    userName, accountNo, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime);
            return null;
        }

        return responseDto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[loan full] data is {}", responseData);

        ResponseDto responseDto = ApiType.LOAN_FULL.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan full] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateLoanFull(responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_FULL.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanFullContentDto> responseDto = (ResponseDto<LoanFullContentDto>) ApiType.LOAN_FULL.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }

}
