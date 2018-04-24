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

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLogLoanAccountService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String loanAccNo, String loanTxNo) {
        QueryLogLoanAccountRequestDto dto = new QueryLogLoanAccountRequestDto(loanAccNo, loanTxNo);

        signatureHelper.sign(dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[query log loan account] sign error, loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);
            return null;
        }

        String responseData = bankClient.send(dto.getRequestData(), ApiType.QUERY_LOG_LOAN_ACCOUNT);
        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[query log loan account] send error, loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[query log loan account] verify sign error, loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);
            return null;
        }

        ResponseDto responseDto = ApiType.QUERY_LOG_LOAN_ACCOUNT.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[query log loan account] parse response error, loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);
            return null;
        }

        return responseDto;
    }
}
