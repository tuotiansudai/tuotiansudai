package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryUserRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryUserContentDto;
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

    private static final ApiType API_TYPE = ApiType.QUERY_USER;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public  QueryUserService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    @SuppressWarnings(value = "unchecked")
    public ResponseDto<QueryUserContentDto> query(String userName, String accountNo) {
        QueryUserRequestDto dto = new QueryUserRequestDto(userName, accountNo);
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return null;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Query User] failed to verify sign, userName: {}, accountNo: {}, response: {}", userName, accountNo, responseData);
            return null;
        }

        return (ResponseDto<QueryUserContentDto>) API_TYPE.getParser().parse(responseData);
    }
}
