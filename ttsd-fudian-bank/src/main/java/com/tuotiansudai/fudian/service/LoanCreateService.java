package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCreateDto;
import com.tuotiansudai.fudian.dto.request.LoanCreateRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCreateContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanCreateService {

    private static Logger logger = LoggerFactory.getLogger(LoanCreateService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_CREATE;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanCreateService(SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankLoanCreateMessage create(BankLoanCreateDto bankLoanCreateDto) {
        LoanCreateRequestDto dto = new LoanCreateRequestDto(bankLoanCreateDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Create] failed to sign, data: {}", bankLoanCreateDto);
            return new BankLoanCreateMessage(false, "签名失败");
        }

        insertMapper.insertLoanCreate(dto);

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Loan Create] failed to verify sign, response data: {}", bankLoanCreateDto);
            return new BankLoanCreateMessage(false, "验签失败");
        }

        ResponseDto<LoanCreateContentDto> responseDto = (ResponseDto<LoanCreateContentDto>) API_TYPE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[Loan Create] failed to parse response data: {}", responseData);
            return new BankLoanCreateMessage(false, "解析银行数据失败");
        }

        this.updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        if (responseDto.isSuccess()) {
            LoanCreateContentDto content = responseDto.getContent();
            return new BankLoanCreateMessage(content.getLoanName(), content.getLoanTxNo(), content.getLoanAccNo(), content.getOrderNo(), content.getOrderDate());
        }

        return new BankLoanCreateMessage(false, responseDto.getRetMsg());
    }
}
