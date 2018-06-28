package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCancelDto;
import com.tuotiansudai.fudian.dto.request.LoanCancelRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCancelContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanCancelService {

    private static Logger logger = LoggerFactory.getLogger(LoanCancelService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_CANCEL;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanCancelService(SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankLoanCancelMessage cancel(BankLoanCancelDto bankLoanCancelDto) {
        LoanCancelRequestDto dto = new LoanCancelRequestDto(bankLoanCancelDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Create] failed to sign, data: {}", bankLoanCancelDto);
            return new BankLoanCancelMessage(false, "签名失败");
        }

        insertMapper.insertLoanCancel(dto);

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Loan Create] failed to verify sign, response data: {}", bankLoanCancelDto);
            return new BankLoanCancelMessage(false, "验签失败");
        }

        ResponseDto<LoanCancelContentDto> responseDto = (ResponseDto<LoanCancelContentDto>) API_TYPE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[Loan Create] failed to parse response data: {}", responseData);
            return new BankLoanCancelMessage(false, "解析银行数据失败");
        }

        this.updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        if (responseDto.isSuccess()) {
            return new BankLoanCancelMessage(bankLoanCancelDto.getLoanId(), dto.getOrderNo(), dto.getOrderDate());
        }

        return new BankLoanCancelMessage(false, responseDto.getRetMsg());
    }
}
