package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryLogLoanAccountRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryLogLoanAccountService {

    private static Logger logger = LoggerFactory.getLogger(QueryLogLoanAccountService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_LOG_LOAN_ACCOUNT;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLogLoanAccountService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String loanTxNo, String loanAccNo) {
        QueryLogLoanAccountRequestDto dto = new QueryLogLoanAccountRequestDto(loanAccNo, loanTxNo);

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return null;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.warn("[query log loan account] failed to verify sign, loanTxNo: {}, loanAccNo: {}, response: {}", loanTxNo, loanAccNo, responseData);
            return null;
        }

        return API_TYPE.getParser().parse(responseData);
    }
}
