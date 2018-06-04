package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryLogAccountRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryLogAccountService {

    private static Logger logger = LoggerFactory.getLogger(QueryLogAccountService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_LOG_LOAN_ACCOUNT;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLogAccountService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String loginName, String mobile, String userName, String accountNo, String queryOrderDate) {
        QueryLogAccountRequestDto dto = new QueryLogAccountRequestDto(loginName, mobile, userName, accountNo, queryOrderDate);

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return null;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.warn("[query log account] failed to verify sign, userName: {}, accountNo: {}, queryOrderDate: {}, response: {}", userName, accountNo, queryOrderDate, responseData);
            return null;
        }

        return API_TYPE.getParser().parse(responseData);
    }
}
