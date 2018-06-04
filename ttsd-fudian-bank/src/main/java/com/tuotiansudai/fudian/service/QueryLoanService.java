package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryLoanRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryLoanContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryLoanService {

    private static Logger logger = LoggerFactory.getLogger(QueryLoanService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_LOAN;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLoanService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    @SuppressWarnings(value = "unchecked")
    public ResponseDto<QueryLoanContentDto> query(String loanTxNo) {
        QueryLoanRequestDto dto = new QueryLoanRequestDto(loanTxNo);

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return null;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.warn("[Query Loan] failed to verify sign, loanTxNo: {}, response: {}", loanTxNo, responseData);
            return null;
        }

        return (ResponseDto<QueryLoanContentDto>) API_TYPE.getParser().parse(responseData);
    }
}
