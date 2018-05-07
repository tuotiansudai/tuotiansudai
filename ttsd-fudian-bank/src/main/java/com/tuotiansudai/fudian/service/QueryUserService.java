package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryUserRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryUserService {

    private static Logger logger = LoggerFactory.getLogger(QueryUserService.class);

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryUserService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String userName, String accountNo, String loginName, String mobile) {
        QueryUserRequestDto dto = new QueryUserRequestDto(accountNo, userName, loginName, mobile);
        signatureHelper.sign(dto, ApiType.QUERY_USER);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[query user] sign error, userName: {}, accountNo: {}", userName, accountNo);
            return null;
        }

        String responseData = bankClient.send(dto.getRequestData(), ApiType.QUERY_USER);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[query user] send error, request data", dto.getRequestData());
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[query user] verify sign error, request data: {}, response: {}", dto.getRequestData(), responseData);
            return null;
        }

        ResponseDto responseDto = ApiType.QUERY_USER.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[query user] parse response error, request data: {}, response: {}", dto.getRequestData(), responseData);
            return null;
        }

        return responseDto;
    }
}
