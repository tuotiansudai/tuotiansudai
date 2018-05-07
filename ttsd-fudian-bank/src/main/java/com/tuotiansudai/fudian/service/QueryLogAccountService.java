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

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLogAccountService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String loginName, String mobile, String userName, String accountNo, String queryOrderDate) {
        QueryLogAccountRequestDto dto = new QueryLogAccountRequestDto(loginName, mobile, userName, accountNo, queryOrderDate);

        signatureHelper.sign(dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[query log account] sign error, userName: {}, accountNo: {}, queryOrderDate: {}", userName, accountNo, queryOrderDate);
            return null;
        }

        String responseData = bankClient.send(dto.getRequestData(), ApiType.QUERY_LOG_ACCOUNT);
        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[query log account] send error, userName: {}, accountNo: {}, queryOrderDate: {}", userName, accountNo, queryOrderDate);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[query log account] verify sign error, userName: {}, accountNo: {}, queryOrderDate: {}", userName, accountNo, queryOrderDate);
            return null;
        }

        ResponseDto responseDto = ApiType.QUERY_LOG_ACCOUNT.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[query log loan account] parse response error, userName: {}, accountNo: {}, queryOrderDate: {}", userName, accountNo, queryOrderDate);
            return null;
        }

        return responseDto;
    }
}
